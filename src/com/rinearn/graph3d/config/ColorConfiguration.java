package com.rinearn.graph3d.config;

import java.awt.Color;

import com.rinearn.graph3d.config.RangeConfiguration.AxisRangeConfiguration;

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
  → とりあえず ColorGradient を仮案の通りに実装したので、ミキサー側を実装したあたりでまた再検討/検証

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

/**
 * The class for storing configuration of colors.
 */
public final class ColorConfiguration {

	/**
	 * The enum for specifying coloring mode for each data series.
	 */
	public enum DataColoringMode {

		/** Represents the mode for drawing a data series with a solid color. */
		SOLID,

		/** Represents the mode for drawing a data series with gradient colors. */
		GRADIENT;
	}

	/** The array storing a color mode for each data series. */
	private volatile DataColoringMode[] dataColoringModes = {
		DataColoringMode.GRADIENT // Because the gradient option is enabled by default.
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

	/** The array storing a gradient color(s) for each of data series. */
	private volatile ColorGradient[] dataColorGradients = {
		new ColorGradient()
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


	/**
	 * Sets the coloring modes, for determining the color of each data series.
	 * 
	 * @param dataColoringModes The array storing a coloring mode for each data series.
	 */
	public synchronized void setDataColoringModes(DataColoringMode[] dataColoringModes) {
		this.dataColoringModes = dataColoringModes;
	}

	/**
	 * Gets the coloring modes, for determining the color of each data series.
	 * 
	 * @return The array storing a coloring mode for each data series.
	 */
	public synchronized DataColoringMode[] getDataColoringModes() {
		return this.dataColoringModes;
	}


	// 以下のやつ、Series って付くのよくないんじゃない？
	// ここで定義されるのはあくまでソリッド色やグラデ色のリストで、
	// インデックスは別に系列インデックスではないよな？ 色番号だよな？
	// 
	// -> いや、mode があってればインデックスはそのまま系列インデックス（%N）に一致するよ。
	//    %Nされるからといって、別に「 n個目のソリッド色 」みたいな感じでインクリメンタルに参照されるわけじゃない。のでこれでいい。
	//
	//    -> それはどちらかというとミキサー側の事情であって、ここでSeriesって決めてしまうと将来性に足枷にならない？
	//       %N ではない方式での色の割り振りとかもあり得るでしょ。N以降は特定色にするとか。
	//
	//       -> そんなら setDataSolidColors とかにすべき？
	//          で、各系列がどの色に割り当てられるかは dataColors[ 系列インデックス % N ] になります、的な。
	//          確かにこっちの方が説明もわかりやすいかも。
	//
	//          -> 全般的に series -> data に改名した。まだ変えてみただけなので仕様が固まるまでこの Note は消さない。

	/**
	 * Sets solid colors used for drawing data series, applied when their coloring modes are SOLID.
	 * 
	 * @param dataSolidColors The array storing a solid color for each data series.
	 */
	public synchronized void setDataSolidsColors(Color[] dataSolidColors) {
		this.dataSolidColors = dataSolidColors;
	}


	/**
	 * Gets solid colors used for drawing data series, applied when their coloring modes are SOLID.
	 * 
	 * @return The array storing a solid color for each data series.
	 */
	public synchronized Color[] getDataSolidColors() {
		return this.dataSolidColors;
	}


	/**
	 * Sets gradient colors used for drawing data series, applied when their coloring modes are GRADIENT.
	 * 
	 * @param dataColorGradients The array storing a ColorGradient instance for each data series.
	 */
	public synchronized void setDataColorGradients(ColorGradient[] dataColorGradients) {
		this.dataColorGradients = dataColorGradients;
	}

	/**
	 * Gets color gradients used for drawing data series, applied when their coloring modes are GRADIENT.
	 * 
	 * @return The array storing a ColorGradient instance for each data series.
	 */
	public synchronized ColorGradient[] getDataColorGradients() {
		return this.dataColorGradients;
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
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {

		// Validate data-coloring modes.
		if (this.dataColoringModes == null) {
			throw new IllegalStateException("The data-coloring modes are null.");
		} else {
			for (DataColoringMode coloringMode: this.dataColoringModes) {
				if (coloringMode == null) {
					throw new IllegalStateException("There is a null element in the data-coloring modes.");
				}
			}
		}

		// Validate data solid colors.
		if (this.dataSolidColors == null) {
			throw new IllegalStateException("The data-solid-colors are null.");
		} else {
			if (this.dataSolidColors.length == 0) {
				throw new IllegalStateException("For data-solid-colors, at least one element is required, but nothing is stored.");
			}
			for (Color solidColor: this.dataSolidColors) {
				if (solidColor == null) {
					throw new IllegalStateException("There is a null element in the data-solid-colors.");
				}
			}
		}

		// Validate data color gradients.
		if (this.dataColorGradients == null) {
			throw new IllegalStateException("The data-color-gradients are null.");
		} else {
			if (this.dataColorGradients.length == 0) {
				throw new IllegalStateException("For data-color-gradients, at least one element is required, but nothing is stored.");
			}
			for (ColorGradient colorGradient: this.dataColorGradients) {
				if (colorGradient == null) {
					throw new IllegalStateException("There is a null element in the data-color-gradients.");
				}
				colorGradient.validate();
			}
		}
	}
}
