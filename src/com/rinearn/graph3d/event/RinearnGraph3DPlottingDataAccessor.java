package com.rinearn.graph3d.event;

import com.rinearn.graph3d.config.data.SeriesAttribute;

/**
 * RinearnGraph3DPlottingListener の実装内から, プロット対象のデータにアクセスするためのインターフェースです.
 * このインターフェースを実装したクラスを, DataAccessor 実装またはデータアクセッサ実装と呼びます.
 */
public interface RinearnGraph3DPlottingDataAccessor {

	/**
	 * このインターフェースを実装したクラスによって提供される, 機能群の世代を返します.
	 *
	 * RINEARN Graph 3D によってプロット可能なデータの種類や並び方などは,
	 * 今後のメジャーバージョンアップ等により, 変化し続ける事が予想されます.
	 * そうなると, データにアクセスする際の手続きや, 前提とすべき設計思想なども変わってくるはずです.
	 * そういった際に, 非互換なレベルの機能群のリフレッシュが行われると, このメソッドが返す番号が上がります.
	 * 互換が保たれるレベルの機能拡張の範囲内では, この番号は変化しません.
	 *
	 * このインターフェースでは, 複数の世代の機能群のメソッドが定義されつつ拡張されていきますが,
	 * それらの中でどれが利用可能なのかは, 実装がこのメソッドに対して返す世代値によって判断してください.
	 *
	 * ただし現時点では, まだ非互換な拡張が行われていないため, 世代は「1」のみが存在します。
	 *
	 * @return 提供される機能群の世代
	 */
	public int getApiGeneration();

	/**
	 * 指定されたデータ点のX座標値を取得します.
	 *
	 * @param seriesIndex データ点が属する系列のインデックス
	 * @param lineIndex データ点が属する線のインデックス
	 * @param pointIndex データ点のインデックス
	 * @return データ点のX座標
	 */
	public double getDataPointX(int seriesIndex, int lineIndex, int pointIndex);

	/**
	 * 指定されたデータ点のY座標値を取得します.
	 *
	 * @param seriesIndex データ点が属する系列のインデックス
	 * @param lineIndex データ点が属する線のインデックス
	 * @param pointIndex データ点のインデックス
	 * @return データ点のY座標
	 */
	public double getDataPointY(int seriesIndex, int lineIndex, int pointIndex);

	/**
	 * 指定されたデータ点のZ座標値を取得します.
	 *
	 * @param seriesIndex データ点が属する系列のインデックス
	 * @param lineIndex データ点が属する線のインデックス
	 * @param pointIndex データ点のインデックス
	 * @return データ点のZ座標
	 */
	public double getDataPointZ(int seriesIndex, int lineIndex, int pointIndex);


	/**
	 * 指定されたデータ系列が可視であるかどうかを判定します.
	 *
	 * @param seriesIndex データ系列のインデックス
	 * @return 可視であれば true
	 */
	public boolean isDataSeriesVisible(int seriesIndex);

	/**
	 * 指定されたデータ線が可視であるかどうかを判定します.
	 *
	 * @param seriesIndex データ線が属する, データ系列のインデックス
	 * @param lineIndex データ線のインデックス
	 * @return 可視であれば true
	 */
	public boolean isDataLineVisible(int seriesIndex, int lineIndex);

	/**
	 * 指定されたデータ点が可視であるかどうかを判定します.
	 *
	 * @param seriesIndex データ点が属する, データ系列のインデックス
	 * @param lineIndex データ点が属する, データ線のインデックス
	 * @param pointIndex データ点のインデックス
	 * @return 可視であれば true
	 */
	public boolean isDataPointVisible(int seriesIndex, int lineIndex, int pointIndex);

	/**
	 * データ系列の総数を返します.
	 *
	 * @return データ系列の総数
	 */
	public int getDataSeriesCount();

	/**
	 * 指定されたデータ系列における, データ線の総数を返します.
	 *
	 * @param seriesIndex データ線が属する, データ系列のインデックス
	 * @return データ線の総数
	 */
	public int getDataLineCount(int seriesIndex);

	/**
	 * 指定されたデータ系列・データ線における, データ点の総数を返します.
	 *
	 * @param seriesIndex データ点が属する, データ系列のインデックス
	 * @param seriesIndex データ点が属する, データ線のインデックス
	 * @return データ点の総数
	 */
	public int getDataPointCount(int seriesIndex, int lineIndex);

	/**
	 * 指定されたデータ系列の属性を返します。
	 *
	 * @param seriesIndex データ系列のインデックス
	 * @return データ系列の属性
	 */
	public SeriesAttribute getDataSeriesAttribute(int seriesIndex);
}
