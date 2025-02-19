/*
 * Author:  RINEARN (Fumihiro Matsui), 2020
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.nano.plugin.system.terminal.TerminalWindow;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class PrintlnXfci1Plugin extends PrintXfci1Plugin {

	public PrintlnXfci1Plugin(boolean isGuiMode, TerminalWindow terminalWindow) {
		super(isGuiMode, terminalWindow);
	}

	// 関数名を返す
	@Override
	public String getFunctionName() {
		return "println";
	}

	// スクリプト実行前の初期化
	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException {
		super.initializeForExecution(engineConnector);

		// 処理系の情報を取得するコネクタ（処理系依存）の互換性を検査
		if (!(engineConnector instanceof EngineConnectorInterface1)) {
			throw new ConnectorException(
				"The type of the engine connector \"" +
				engineConnector.getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}
		EngineConnectorInterface1 eci1Connector = (EngineConnectorInterface1)engineConnector;

		// 処理系のオプションから、端末I/O用のデフォルトの改行コードを取得し、print 終端文字に設定
		//（この値が PrintXfci1Plugin では空文字で、両者はその点のみが異なる）
		if (eci1Connector.hasOptionValue("TERMINAL_IO_EOL")) {
			this.printEnd = (String)eci1Connector.getOptionValue("TERMINAL_IO_EOL");
		} else {
			this.printEnd = System.getProperty("line.separator");
		}
	}

	// 残りは PrintXfci1Plugin と全く同じ処理
}
