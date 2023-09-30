/*
 * Author:  RINEARN (Fumihiro Matsui), 2020
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class ExitVoidXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	// パーミッション要求用にエンジンコネクタを控える
	EngineConnectorInterface1 engineConnector = null;

	// 初期化/終了時処理の引数に渡される、スクリプトエンジンと情報をやり取りするインターフェースの指定
	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}

	// 接続時の初期化
	@Override
	public void initializeForConnection(Object engineConnector) throws ConnectorException { }

	// スクリプト実行前の初期化
	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException {

		// 処理系の情報を取得するコネクタ（処理系依存）の互換性を検査し、適合すればフィールドに控える
		if (!(engineConnector instanceof EngineConnectorInterface1)) {
			throw new ConnectorException(
				"The type of the engine connector \"" +
				engineConnector.getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}
		this.engineConnector = (EngineConnectorInterface1)engineConnector;
	}

	// スクリプト実行後の終了時処理
	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	// 接続解除時の終了時処理
	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }


	// 関数名を返す
	@Override
	public String getFunctionName() {
		return "exit";
	}

	// 引数は取らないので空配列を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[0];
	}

	// データの自動変換を有効化しているので参照されない
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return null;
	}

	// 引数名は定義されていないので false を返す
	@Override
	public boolean hasParameterNames() {
		return false;
	}

	// 引数名を返すが、引数が無いので空配列を返す
	@Override
	public String[] getParameterNames() {
		return new String[0];
	}

	// 任意型の引数を取る場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[0];
	}

	// 任意次元の引数を取る場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[0];
	}

	// 参照渡しする必要がある場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[0];
	}

	// 引数の中身を書き変えない場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[0];
	}

	// 任意個の引数は取らないので false を返す
	@Override
	public boolean isParameterCountArbitrary() {
		return false;
	}

	// 可変長引数は取らないので false を返す
	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	// 戻り値は無いので、void 型を返す
	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return void.class;
	}

	// データの自動変換を有効化しているので参照されない
	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return null;
	}

	// 戻り値のデータ型は固定なので false
	@Override
	public boolean isReturnDataTypeArbitrary() {
		return false;
	}

	// 戻り値の配列次元数は固定なので false
	@Override
	public boolean isReturnArrayRankArbitrary() {
		return false;
	}

	// データ型の自動変換機能は利用するので true を返す
	@Override
	public boolean isDataConversionNecessary() {
		return true;
	}

	// スクリプトから呼ばれた際に実行する処理
	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// プログラムの終了を許可/拒否設定できるパーミッション項目が存在するため、終了前に要求しておく
		// (却下されてもどのみち permission denied で終了するが、エラー扱いになるかどうかが異なる)
		// (許可されればこの行では何も起こらず、拒否されればここで ConnectorException が発生する）
		this.engineConnector.requestPermission(ConnectorPermissionName.PROGRAM_EXIT, this, new String[0]);

		// 許可された場合、exit 関数による正常終了はエラー扱いにしたくないため、
		// 特別に "__EXIT:終了ステータスコード" のメッセージを持たせた ConnectorException を投げる
		// (そうするとスクリプトエンジン側がそう判断してくれる)
		throw new ConnectorException("___EXIT:0");
	}
}
