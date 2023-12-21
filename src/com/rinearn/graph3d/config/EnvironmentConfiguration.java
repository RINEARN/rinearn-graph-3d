package com.rinearn.graph3d.config;

import java.util.Locale;

// !!! NOTE !!!
// 命名は EnvironmentConfiguration と SystemConfiguration とどっちが無難？
//
// ・ SystemConfiguration だと漠然とした範囲がより広い感じで、UIフォントとか含んでそうな気がする。
//    しかし実際はUIフォントは恐らく FontConfiguration に格納するので、
//    こっちもそこまで漠然さを拡げない方が良い気がする。
//
// ・ メニューバーで Set System よりかは Set Environment の方がまだマシになるかも。
//     -> それはもうメニュー名を System Settings と Environment Settings にすればいいので別にどっちでも良さそう。
//        その表記まで Ver.5 と互換とらなくてもいい。
//
// ・ スクリプト周りのリソースフォルダのパスとか設定するのは Environment の方が合ってそうな印象が強い。
//
// ・逆に Environment のスコープに含まず System のスコープに含むものの例は？
//
//     ・デフォルトのウィンドウサイズとか。
//       System Configuration にあるのはしっくりくるが、Environment だとなんか微妙な気がする。環境関係ないし。
//       まあ適切値がモニターサイズに依存すると考えれば強引に納得はできなくもないが
//
//       -> なら Environment 案では、
//          上記は WindowConfiguration を作って Window Settings メニューから設定するようにすべきか。
//          ちょうどスクリーン横の操作パネルの表示/非表示とかもUI上で設定できた方がいいのでありかも。
//
//  -> 結局、Environment のスコープに含めると違和感あるけど System だとしっくりくるやつって、
//     本来は別のスコープが新規にあるべきで、そっちに属した方がむしろ良いという感じのやつなのでは？
//     System のスコープが意味空間で横断的でスクエアじゃないだけかもしれない。SystemだとAxisの二の舞になるかも。
//
// また後で再検討
//
// !!! NOTE !!!


/**
 * The class for storing configuration parameters depending on the user's environment and basic preferences.
 */
public final class EnvironmentConfiguration {

	/** The Locale for switching the language of UI of this application. */
	private volatile Locale locale = Locale.getDefault();


	/**
	 * Creates a new configuration storing default values.
	 */
	public EnvironmentConfiguration() {
	}


	/**
	 * Sets the Locale for switching the language of UI of this application.
	 *
	 * @param locale The Locale for switching the language of UI.
	 */
	public synchronized void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets the currently set Locale, which determines the language of UI of this application.
	 *
	 * @param locale The currently set Locale.
	 */
	public synchronized Locale getLocale() {
		return this.locale;
	}

	/**
	 * Checks whether the language/country code of the Locale of this instance represents the Japanese environment.
	 *
	 * @return Returns true only when the Locale represents the Japanese environment.
	 */
	public synchronized boolean isLocaleJapanese() {
		return (this.locale.getLanguage()!=null && locale.getLanguage().equals("ja"))
			|| ( locale.getCountry()!=null && locale.getCountry().equals("JP"));
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
		if (this.locale == null) {
			throw new IllegalStateException("The locale is null.");
		}
	}
}
