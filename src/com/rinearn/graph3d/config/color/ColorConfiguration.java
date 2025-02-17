package com.rinearn.graph3d.config.color;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;
import java.awt.Color;

/*

!!!!!
NOTE
!!!!!

・カラーミキサーのグラデーション設定とかブレンド処理とか、どこに属するべきか？

  -> レンダラーにメソッドがある時点でConfigに持たせるしかない気がする。
     本来オプションからの描画生成は上の層だし、レンダラーからオプションの設定を任意タイミングで読めるのはおかしいし。
     なので、設定の格納はConfig一択なような。引数paramの設定に応じて系列色になったりグラデになったりの実装も考えると。
     (つまり現行版でそれがオプション層にあるのはそもそもなんかおかしい)

  ↑いや、それ自体は、ブレンドの処理と設定メソッドがレンダラー層にあれば、
    上層からオプションON/OFFする度に設定参照＆更新してるって解釈も、仕組み的に可能なのは可能でしょ。
    Config管轄ってのは別にマストではない。レンダラーに直接 Gradient の Setter があってもいい。あくまで理屈の整合上はだけど。

  ↑いやしかし、上限/下限とか、彩色次元とか、色々複雑なパラメータあるし、
    設定構造の規模から考えたらそれ自体をColorConfigとは別にGradientConfigってしても良いレベルかも。むしろ。
    まあ少なくともconfig階層に属すべきだとは思う。規模的に。

  ↑上記の続き:
    表面的なオプション層との対応関係や互換性は、あくまでもカテゴリーごとの保存と再現ができれば実用問題は生じないので、
    エンコーダ/デコーダでどうとでも吸収できる。オプションの保存はOptionConfigでON/OFF状態さえ保存すれば済むし、
    その際にグラデの色設定は ColorConfig に書き込み/読み込みした所で、表面的な問題は何も生じない。中での話で閉じるし。

    なので将来性を考えると内部Configは素直に作っとといた方が良い。設定ファイルは上記の通り、互換はどうにでもできるので。変換も可能だし。
    それに、設定ファイルの中にコメント吐けるので、オプションの下に「これの詳細設定は ... の項目にある」とかも書いてnotationできるし。

    という事でConfig管轄で。

  ↑まあそもそもの原因としては現行版でGradientがオプションにあるのがなんかおかしいので。
    しかし便利さを考えたらそちらのほうが便利かもだけど。
    外から見える層ではまあそれでもいいか。中は中で整頓すれば。必ず内外が対応している必要はない。


・そういえばレンダラー層の param の autoColoringEnabledは、
  Gradientだけでなく系列でのソリッド彩色も（オプション状態に応じて）行われる仕様なんだった。
  つまりカラーミキサはグラデとソリッド彩色の両者を併せて扱わないと。両者を分けるのはまずい。
  とするとConfigも、「ソリッド色設定だけConfigにあってグラデ色設定はオプション側にある」みたいに分かれるのはやはりまずい。

  -> 例えばColorConfigの中にGradientColorConfigとSolidColorConfigがあって、ColoringModeで選択する。
     んでGradientConfigのsetは、オプションUIからもSet Colorメニューからも行えるようにすればOKか。
     保持はConfig層、setはオプションからも可、という。


・グラデには恐らくデフォルト（系列ごと設定が無い場合の採用値）と系列ごとの設定の2通りが必要になるように、ソリッドにも要る？
  引数 param で系列インデックスを指定していない場合にデフォルトが採用される、とかで使える。
  または、指定した系列インデックスに対応する色設定が存在しない場合。

  ↑後者は系列インデックスについて色が循環的になる挙動と整合しなくない？そこは%Nして参照すべきでは？
    > 確かに。

  → ソリッドの登録数とグラデの登録数が同じである必要はないんだし、グラデは1個だけ登録しとけば、
     （i % 1 = i for 任意の i なので）全系列が同じグラデになって、
     その場合デフォルト（系列ごと設定が無い場合の採用値）と同じように機能するでしょ。
     なので、そもそもデフォルトという概念が要らない。1個だけ登録っていう状態で兼ねられる。

     んで、そのグラデ設定の1個を、現行版のデフォルトのやつにしとけばいい。
     ソリッドはデフォルトで8個くらい用意しといて。
     そうすりゃ「グラデONだと全系列デフォルトグラデ、OFFだと系列ごと彩色」という現行版の挙動を踏襲しつつ、
     必要に応じて「グラデを系列ごとに分けたい」といった場面での拡張性も持たせられる。

     ↑これいい。たぶんこれを採用する。

     → しかし、ColorMode は系列ごとに設定できるべきだけど、そうするとソリッドとグラデの定義数が同じじゃないといけなくない？

        → colorMode も %N して使う仕様にすればいい。別に同じである必要はない。

           つまり、デフォルトでソリッドが8色、グラデが1色されてたとする。
           この時にユーザーが系列0,1,2までのデータをプロットしていたとして、colorModeは要素数3で指定すれば十分で。
           それ超えたら[0]って循環する。

           ↑ 上の例だとグラデ→ソリッド→ソリッドになって、その後またグラデ→ソリッド→ソリッドになるみたいな。
              んでそれぞれのグラデやソリッドがどの色になるかは、それぞれの色設定に %N して算出される感じで。

              ややこしいけど、まあこの設定の中身はUIが生成するので、UI側で見え方を調整すればいけるか。
              構造的な柔軟性はたぶん高いのであり。


・Gradient、2次元グラデ（レインボー＋透明度）とかもリクエストあったんだし、
  単純なグラデはGradient1Dとかにすべきか。後々で拡張可能にするために。2次元グラデをサポートする時にGradient2D作る。

  ↑確かに、X/Y/Z値でそれぞれ違う成分をグラデさせる、とかもありえるわけだし、Gradient3Dとかも出てくる。

    ↑となると、必然的に Min とか Max とか Dimension は Gradation1D オブジェクトが持つべきか。
      ColorConfig がトップ階層で持つのはよくない。

  → しかし、GradientColorをインターフェースにするよりは、実装にして、
     gradientModeを持たせて分派したロジックも持たせる方がいい気もする。
     Config層でインターフェース＋大量の実装みたいな形は全体把握がうっとうしそうでなんかいやな気がする。

     → しかし実装のすっきり感としては Gradient はインターフェース＋実装分派で分けた方が合ってる気がする。なんとなくだけど。

     ↑ GradientColor がミキサーを兼ねるなら後者だと思う。Configに徹してミキサーは別実装なら前者でもいいかも。


  また実装しながら後々で要検討
  → とりあえず GradientColor を仮案の通りに実装したので、ミキサー側を実装したあたりでまた再検討/検証

!!!!!
NOTE
!!!!!
*/


/*
!!!!!
NOTE 2
!!!!!

・setColorBarRange 的な手軽なやつが欲しい。

  → config の層は構造的に合わなさそう。RinearnGraph3Dにダイレクトに付ける？
     内部処理的には、axisGradients[0] の autoRangingEnabled を false にしつつ min/max 値を設定する config 操作を一括でやる感じで、
     できるのは一応できる。問題はあっていいかどうか。しかし無しという選択肢は実用考えたら厳しい気がするし、やっぱあるべきか。

	 また後々で要検討。

!!!!!
NOTE 2
!!!!!
 */


/*
!!!!!
NOTE 3
!!!!!

・現状のこの構造だと、グラデーションとソリッド彩色の系列が共存するのが無理じゃない？ そういう場面は往々にしてありそうだが。

　→ 一応はグラデーションでソリッドも表現できるので全く不可能ではないが… 大げさすぎるか。シンプルさに欠ける。(※1)

　→ とすると、ソリッド彩色かグラデーションかを分けられる要素を作って、その配列を持つべき？ 現状とは階層が逆になる感じで。

　→ それはそれで全体一括グラデーションがむずい。のと「グラデーション」オプションのUIとの整合をうまく取れるか？

　→ ※2: いや、意外と「※1」が良案な気がしてきた。
　　　　　グラデーションをONにしてる時点で、彩色設定が多少は複雑化する事はどうしても避けられないので、
　　　　　そっからさらにソリッドと併用するためのメタな構造を作って理解を強いるよりも、
　　　　　「実質単色なグラデ」を手軽に作れるUIを付けてしまった方がいいかもしれない。
　　　　　そういう「エレガントではないが直球ゴリ押し」な手は、意外と多くのユーザーに好まれる。

　　　　・逆に、最も構造的に整うのは
　　　　　「グラデーションやソリッド色を必用な数定義して、各系列からそれをグラデ番号/ソリッド色番号等で参照する」
　　　　　という方式だろうけれど、そういう「抽象概念への参照を飛ばす系」は一番嫌われるパターンだよなあ。
　　　　　参照を挟むと、設定している事の「しっくり感」が一気に飛んでしまう。そういうのは一部のメタ概念強強erしか好まん気がする。

　ともかく確定まで十分悩む必要あり

!!!!!
NOTE 3
!!!!!
 */


/*
!!!!
NOTE 4  (2024/01/10～)
!!!!

単色/グラデーションのモードを系列ごとに持たせる設計はやっぱり悪手かもしれん。現状の dataSeriesColoringModes の件。
「グラデーションオプションを ON にするか OFF にするかで、全体が単色彩色かグラデ彩色かが切り替わる」ってのは根幹的な彩色UIコンセプトでしょ。
表層の設定画面デザインの範疇とかではなくて。

とすると、色設定画面も「グラデーションON時の設定」と「グラデーションOFF時の設定」とで分けて設定できるべきでは。
そして、「グラデーションON時の設定」では、単色は同色グラデとして作成できるんだし。

-> いやいやそれは今のUIにとらわれすぎてる。実際、系列1は単色、系列2と3はグラデ、系列4は単色… みたいにプロットしたい場合は普通によくあるでしょ。
   それを現状カバーできてないのが大問題なわけで。つまり現状のUIが結構問題あるんだよ。思い出せ。

   -> ああ確かに。再考したらそんな気がしてきた。やっぱり当時ちゃんと考えた結果そうすべきって着地点だったのか。
      そんならグラデーションオプションはどういう挙動を対応させる？

      -> ON にしたらカラー設定画面の系列をぜんぶグラデにして、OFF にしたら全部単色にする、みたいな。
         んでカラー設定画面から系列ごとにマニュアル設定する場合は、グラデーションオプションをOFFにしてください、みたいな。

      -> いっそカラー設定の最上階層に、「全系列をソリッド彩色」「全系列をグラデ彩色」「系列ごとに分ける」みたいなモードを設けては。
         そんで Ver.6.0 では前者2つのみサポートすれば Ver.5.6 同等には到達できる。そして後の 6.x でよく考えながら拡張。

         -> これいい案かも。とりあえず採用

!!!!
NOTE 4
!!!!
*/

/**
 * The class for storing configuration of colors.
 */
public final class ColorConfiguration {

	/** The flag to enable/disable the gradient colors on the data coloring. */
	private volatile boolean dataGradientColorEnabled = true;

	/** The array storing a gradient color(s) for each of data series. */
	private volatile GradientColor[] dataGradientColors = {
		new GradientColor()
	};

	/** The array storing a solid color for each data series. */
	private volatile Color[] dataSolidColors = {
		Color.RED,
		Color.GREEN,
		Color.BLUE,
		Color.MAGENTA,
		Color.YELLOW,
		Color.CYAN,
		Color.PINK,
		Color.ORANGE
	};

	/** The background color of the graph screen. */
	private volatile Color backgroundColor = Color.BLACK;

	/** The foreground color, which is a color of the outer frame of the graph, labels, and so on. */
	private volatile Color foregroundColor = Color.WHITE;

	/** The color of the grid lines frame of the graph. */
	private volatile Color gridColor = Color.DARK_GRAY;


	/**
	 * Creates new configuration storing default values.
	 */
	public ColorConfiguration() {
	}

	public synchronized void setDataGradientColorEnabled(boolean dataGradientColorEnabled) {
		this.dataGradientColorEnabled = dataGradientColorEnabled;
	}

	public synchronized boolean isDataGradientColorEnabled() {
		return dataGradientColorEnabled;
	}

	/**
	 * Sets solid colors used for drawing data series.
	 *
	 * When dataGradientColorEnabled is false,
	 * each data series are drawn using each solid color in the specified array.
	 * The index of a solid color for a data series is determined as:
	 *   solidColorIndex = dataSeriesIndex % solidColorCount,
	 * where solidColorCount is the total number of the solid colors.
	 *
	 * @param dataSolidColors The array storing a solid colors.
	 */
	public synchronized void setDataSolidsColors(Color[] dataSolidColors) {
		this.dataSolidColors = dataSolidColors;
	}


	/**
	 * Gets solid colors used for drawing data series.
	 *
	 * When dataGradientColorEnabled is false,
	 * each data series are drawn using each solid color in the returned array.
	 * The index of a solid color for a data series is determined as:
	 *   solidColorIndex = dataSeriesIndex % solidColorCount,
	 * where solidColorCount is the total number of the solid colors.
	 *
	 * @return The array storing a solid colors.
	 */
	public synchronized Color[] getDataSolidColors() {
		return this.dataSolidColors;
	}


	/**
	 * Sets gradient colors used for drawing data series.
	 *
	 * When dataGradientColorEnabled is true,
	 * by default, all the gradient color of the specified array are applied to all the data series.
	 * To coloring each data series separately, use series filter feature of each gradient color.
	 *
	 * @param dataGradientColors The array storing gradient colors.
	 */
	public synchronized void setDataGradientColors(GradientColor[] dataGradientColors) {
		this.dataGradientColors = dataGradientColors;
	}

	/**
	 * Gets gradient colors used for drawing data series.
	 *
	 * When dataGradientColorEnabled is true,
	 * by default, all the gradient color of the returned array are applied to all the data series.
	 * To coloring each data series separately, use series filter feature of each gradient color.
	 *
	 * @return The array storing gradient colors.
	 */
	public synchronized GradientColor[] getDataGradientColors() {
		return this.dataGradientColors;
	}

	/**
	 * Sets the background color of the graph screen.
	 *
	 * @param backgroundColor The background color of the graph screen.
	 */
	public synchronized void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the background color of the graph screen.
	 *
	 * @return The background color of the graph screen.
	 */
	public synchronized Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Sets the foreground color, which is a color of the outer frame of the graph, labels, and so on.
	 *
	 * @param foregroundColor The foreground color.
	 */
	public synchronized void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	/**
	 * Gets the foreground color, which is a color of the outer frame of the graph, labels, and so on.
	 *
	 * @return The foreground color.
	 */
	public synchronized Color getForegroundColor() {
		return this.foregroundColor;
	}

	/**
	 * Sets the color of grid lines.
	 *
	 * @param gridColor The color of grid lines.
	 */
	public synchronized void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	/**
	 * Gets the color of grid lines.
	 *
	 * @return The color of grid lines.
	 */
	public synchronized Color getGridColor() {
		return this.gridColor;
	}

	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {

		// Validate data solid colors.
		if (this.dataSolidColors == null) {
			throw new RinearnGraph3DConfigurationException("The data-solid-colors are null.");
		} else {
			if (this.dataSolidColors.length == 0) {
				throw new RinearnGraph3DConfigurationException("For data-solid-colors, at least one element is required, but nothing is stored.");
			}
			for (Color solidColor: this.dataSolidColors) {
				if (solidColor == null) {
					throw new RinearnGraph3DConfigurationException("There is a null element in the data-solid-colors.");
				}
			}
		}

		// Validate data color gradients.
		if (this.dataGradientColors == null) {
			throw new RinearnGraph3DConfigurationException("The data-gradient-colors are null.");
		} else {
			if (this.dataGradientColors.length == 0) {
				throw new RinearnGraph3DConfigurationException("For data-gradient-colors, at least one element is required, but nothing is stored.");
			}
			for (GradientColor colorGradient: this.dataGradientColors) {
				if (colorGradient == null) {
					throw new RinearnGraph3DConfigurationException("There is a null element in the data-gradient-colors.");
				}
				colorGradient.validate();
			}
		}
	}
}
