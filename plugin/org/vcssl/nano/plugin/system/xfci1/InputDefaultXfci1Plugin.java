/*
 * Author:  RINEARN (Fumihiro Matsui), 2021
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class InputDefaultXfci1Plugin extends InputXfci1Plugin {

	// string型の引数と任意型の引数を取るので String 型, Object型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class, Object.class };
	}

	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] {
			ArrayDataAccessorInterface1.class,
			ArrayDataAccessorInterface1.class
		};
	}

	// 引数名を返す
	@Override
	public String[] getParameterNames() {
		return new String[] { "message", "defaultValue" };
	}

	// 任意型の引数は、2つめの引数だけが取るので false, true を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false, true };
	}

	// 任意次元の引数は取らないので false を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false, false };
	}

	// 引数が参照渡しされる必要はないので false を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false, false };
	}

	// 引数の中身を書き変えないので true を返す（参照渡しの場合はそう宣言しないと、リテラル等を引数に取れない上に、最適化でも少し不利になる）
	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true, true };
	}

	// スクリプトから呼ばれた際に実行する処理
	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// 渡される arguments の要素数やデータ入出力インターフェースは、
		// このクラスの場合は確定しているはずで、
		// ArrayDataContainerInterface 型で要素数は 3 個、
		// その内容は [0] が戻り値格納用、[1], [2] がスクリプトから渡された 実引数。
		if (arguments.length != 3
				|| !(arguments[0] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[1] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[2] instanceof ArrayDataAccessorInterface1) ) {
			throw new ConnectorException("The number or types of arguments is/are unexpected");
		}

		// arguments[1] に表示メッセージが String[] 型（スカラなので要素数は 1 ）で格納されているので取り出す
		ArrayDataAccessorInterface1<?> messageDataContainer = (ArrayDataAccessorInterface1<?>) arguments[1];
		Object messageData = messageDataContainer.getArrayData();
		String message = ((String[])messageData)[ messageDataContainer.getArrayOffset() ];

		// arguments[2] にデフォルト値が何らかの配列型（スカラなので要素数は 1 ）で格納されているので取り出す
		ArrayDataAccessorInterface1<?> defaultValueDataContainer = (ArrayDataAccessorInterface1<?>) arguments[2];
		Object defaultValueData = defaultValueDataContainer.getArrayData();
		String defaultValue = null;
		if (defaultValueData instanceof long[]){
			defaultValue = Long.toString( ((long[])defaultValueData)[ defaultValueDataContainer.getArrayOffset() ] );
		} else if (defaultValueData instanceof double[]){
			defaultValue = Double.toString( ((double[])defaultValueData)[ defaultValueDataContainer.getArrayOffset() ] );
		} else if (defaultValueData instanceof boolean[]){
			defaultValue = Boolean.toString( ((boolean[])defaultValueData)[ defaultValueDataContainer.getArrayOffset() ] );
		} else if (defaultValueData instanceof String[]){
			defaultValue = ((String[])defaultValueData)[ defaultValueDataContainer.getArrayOffset() ];
		} else {
			throw new ConnectorException(
				"Unsupported internal data type: " + defaultValueData.getClass().getCanonicalName()
			);
		}

		// ユーザーによる入力処理（InputXfci1Plugin で実装されている）
		String inputtedValue = this.input(message, defaultValue);

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、格納する
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<String[]> returnDataContainer = (ArrayDataAccessorInterface1<String[]>)arguments[0];
		String[] returnArrayData = new String[] { inputtedValue };
		int returnArrayOffset = 0; // スカラを表す場合に配列データ内での格納位置を指すが、配列を表す場合は常に 0
		int[] returnArrayLengths = new int[] { 1 }; // 次元ごとの要素数を表す配列で、戻り値は [1] なので { 1 }
		returnDataContainer.setArrayData( returnArrayData, returnArrayOffset, returnArrayLengths );

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、このメソッドの戻り値は参照されない
		return null;
	}
}
