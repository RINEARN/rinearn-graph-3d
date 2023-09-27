package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;


//!!! NOTE !!!
//
//Configuration を設定するメソッドがあちこちにあるけど、マージ挙動になるのかセット挙動になるのかが現状ややこしい。
//どの階層ではマージするのか、方針や基準を明確にすべき。
//
//-> 外からのAPIの利便性としてはマージなんだけど（部分設定とかするし）、内部のコンポーネントでいちいち毎回マージする必用はない。
//
//-> とすると外から設定受けとる RinearnGraph3D と同Rendererとその実装はマージ、
//  それ以外の内部コンポーネントはセット挙動、というのが処理的には妥当そう。
//
//-> 基準は↑で、あと命名も分けるとか。マージ挙動は configure、セット挙動は setConfiguration で。
//  後者はそもそも英語的に微妙で、全部 configure に直して統一する案もあったけど、
//  外部API用の configure と挙動が違う事を暗示できるし、内部コンポーネントはあえて setConfiguration で残すのよさそう。
//
//  "setConfiguration" は config コンテナ自体をセットするけど、"configure" は設定を行うという事自体を提供する雰囲気だし。
//  前者は、必用な要素が欠けた部分設定をセットするとエラー投げる。後者だと部分設定でもマージしてインクリメンタルに反映する。
//  慣れればまあまあ直感的に馴染めそう。
//
//-> そもそも View 層のコンポーネントが内部に config を保持し続ける事自体はいいのか？
//reflectConfiguration(config) とかで反映させるだけにして、内部では保持しない手もあるのでは？
//config を保持するのは Model 層に留めて、他は必用なタイミングで Presenter 経由で渡されて、格納はしない、って方がいいのでは？
//
//-> かもしれん。また要検討
//
//   -> ↑だとレンダラーから config 除去する事になるのでは？ 無理がある気が。
//
//       -> レンダラーは一番高速に回るやつなので内部で config 持ってないとさすがにきつい。
//          巨大なステートマシンだし、APIで直接操作する場面もあるやつだし。
//          でもこのアプリだとレンダラーは一応 view とは別の管轄というふうに境界切ってるので、それはもう特例でいい。
//          アプリの構造とは少し独立したやつという扱いで。
//
//-> 色々考えたら確かに ↑x4 の「View は config 保持しない」方針がベスト、というか本来目指すべきは当然そっちという気がする。
//  持つのは最初から妥協しすぎというか。とりあえず↑x4 案でいって、無理が生じたら setConfiguration 案に繰り戻すのがよさそう。
//
//  -> そもそも少し前まで View は無 config だったのがなぜ突然この混乱した流れになったのか？
//     -> この LabelSettingWindow で言語/フォント情報要るから config 参照しないといけないので引っ張って来ないとという。安直に。
//        -> ↑は限られたタイミングの反映処理でいけるので変わったタイミングで Presenter から reflect 的なのに渡せばいい、
//           保持し続ける必要は全くない。やはり。
//
//           というかViewは受動的な層なので、自由なタイミングで自発的に config 読めてうれしい時は恐らくほぼないはず。
//           処理は必ず別の層からトリガされるので必用ならその時config渡してもらえば済むはず。
//
//-> それなら命名は reflectConfiguration ではなくシンプルに configure でいいのでは？
//マージ挙動かセット挙動かの判断がややこしいという問題が再燃して堂々巡りになるかも。
//setConfiguration はセット挙動である事を明示しやすいけど、set のくせして内部に保持しないのも微妙だし。
//
//-> 確かにそうだが、命名をわかりやすくするために実装側が合わせるにしては影響がでかすぎる。
//  そのためだけにView全体がconfig持つのは。構造的に悪い方向だし。
//  あーでも無意識的にそういう気分で違和感消すためにViewにconfig持たせようとしたかもしれない。いま考えると。
//
//-> マージ挙動かセット挙動かややこしい問題に戻ると、仮に View 側命名を configure に統一しても、
//  Viewが内部で config を保持しないという方針を前提として決めてしまえば、
//  そもそもマージする元が無いのでマージ挙動ではない事は自明なのでは。
//  というか保持しないんだからセット挙動ですらない。読んで、自信に影響があるパラメータが入ってれば反映するだけ。
//
//  -> ああそっか影響あるやつ入ってなくてもエラーじゃなくて読み飛ばすようにすればマージ挙動との混乱はそれほどないか
//
//     -> しかしView層はAPI提供しないので、config降って来る時って内部フローで降って来るので、
//        入ってなければおかしいしエラー出てほしい。というか絶対出るべき。明らかに何かミスってるわけだし。
//
//        -> なら結局
//           「APIの入口だけマージ挙動、それ以外はセットか直接反映挙動で、後者は必用な要素が欠けてたらエラーになる」
//           という基準を決めて、命名は configure か。
//
//        とりあえず上記↑でいってまずかったらまた再検討
//
//!!! NOTE !!!

//!!! NOTE !!!
//
// ・APIリクエストを処理する Presenter 内の各ハンドラが、原則としてイベントディスパッチ上で処理するようになったので、
//   この View 層内コンポーネントの各 setter はイベントディスパッチスレッドに仲介して投げる必要はもうほぼなさそう。
//   むしろ、誤って Presenter 層を経由せずにView層にアクセスしないためにも、
//   caller thread がイベントディスパッチスレッドじゃなければエラーで弾いてもいいくらいかもしれない。
//
//   -> しかし、Presenter層の中でもヘビーな処理はさらに別スレッドで処理するケースが今後生じるが、
//      そうなった時にそこからView操作する時に面倒くない？
//      Presenter 層内のスレッド関連はまだ二転三転する可能性があるので、固まるまでは残しておいてもいいのでは。
//
//      -> 独立スレッドから直接View層を触るってのがそもそもややこしさの種になるかも。
//         そういうのは限定ケースなので、多少手間でも Presenter の中でイベントディスパッチスレッドに移ってから
//         View を操作すべきでは？ そうしないとできないっていう制約があった方がむしろいい気がする。
//         限定ケースで済むなら。至る所でそれやるとなると考えものだけど。
//
// 要検討
//
//!!! NOTE !!!


/**
 * The front-end class of "View" layer (com.rinearn.graph3d.view package)
 * of RINEARN Graph 3D.
 * 
 * View layer provides visible part of GUI, without event handling.
 */
public final class View {

	/** The main window of RINEARN Graph 3D (on which a 3D graph is displayed). */
	public final MainWindow mainWindow = new MainWindow();

	/** The label setting window. */
	public final LabelSettingWindow labelSettingWindow = new LabelSettingWindow();

	/** The range setting window. */
	public final RangeSettingWindow rangeSettingWindow = new RangeSettingWindow();

	/** The light setting window. */
	public final LightSettingWindow lightSettingWindow = new LightSettingWindow();


	// !!! IMPORTANT NOTE !!!
	//   Don't store the configuration as a field of this instance or subcomponents. It is Model layer's role.
	//   Components in View layer passively reflect the configuration propagated from Model through Presenter layer,
	//   without storing it. So don't declare the following:
	// --------------------
	// RinearnGraph3DConfiguration config;


	/**
	 * Creates new View layer of RINEARN Graph 3D.
	 */
	public View() {
	}


	/**
	 * Reflects the configuration parameters related to the components in View layer.
	 * 
	 * @param configuration The configuration container.
	 */
	public synchronized void configure(RinearnGraph3DConfiguration configuration) {
		this.mainWindow.configure(configuration);
		this.labelSettingWindow.configure(configuration);
		this.rangeSettingWindow.configure(configuration);
		this.lightSettingWindow.configure(configuration);
	}
}
