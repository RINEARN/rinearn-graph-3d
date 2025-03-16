package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;

import java.awt.Color;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.plotter.ContourPlotterConfiguration;


/**
 * The "plotter" to plot each data series as a mesh.
 *
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class ContourPlotter implements RinearnGraph3DPlottingListener {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	@SuppressWarnings("unused")
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	@SuppressWarnings("unused")
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


	/**
	 * Create a new instance performing plottings using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 * @param renderer The rendering engine of 3D graphs.
	 */
	public ContourPlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		this.renderer = renderer;
	}


	/**
	 * Called when a plotting/re-plotting is requested.
	 *
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingRequested(RinearnGraph3DPlottingEvent event) {
		// Get the configuration of "With Membranes" option.

		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		ContourPlotterConfiguration contourPlotterConfig = plotterConfig.getContourPlotterConfiguration();
		boolean isContourOptionEnabled = contourPlotterConfig.isPlotterEnabled();

		// Plots all data series.
		// This plotter do nothing if "With Membranes" option is not selected.
		if(!isContourOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = contourPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? contourPlotterConfig.getSeriesFilter() : null;

		// Plots all data series.
		DataSeriesGroup<AbstractDataSeries> dataSeriesGroup = this.model.dataStore.getCombinedDataSeriesGroup();
		int dataSeriesCount = dataSeriesGroup.getDataSeriesCount();
		for (int dataSeriesIndex=0; dataSeriesIndex<dataSeriesCount; dataSeriesIndex++) {

			// Filter the data series.
			SeriesAttribute seriesAttribute = dataSeriesGroup.getDataSeriesAt(dataSeriesIndex).getSeriesAttribute();
			if (existsSeriesFilter && !seriesFilter.isSeriesIncluded(seriesAttribute)) {
				continue;
			}

			// Plot.
			AbstractDataSeries dataSeries = dataSeriesGroup.getDataSeriesAt(dataSeriesIndex);
			this.plotContour(dataSeries, dataSeriesIndex, contourPlotterConfig);
		}
	}


	/**
	 * Plots the specified data series as a mesh.
	 *
	 * @param dataSeries The data series to be plotted.
	 * @param seriesIndex The index of the data series.
	 * @param lineWidth The width (in pixels) of lines composing a mesh.
	 */
	private void plotContour(AbstractDataSeries dataSeries, int seriesIndex, ContourPlotterConfiguration contourPlotterConfig) {
		if (contourPlotterConfig.isAutoRangeEnabled()) {
			RangeConfiguration rangeConfig = this.model.config.getRangeConfiguration();
			AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
			contourPlotterConfig.setMinimumCoordinate(zRangeConfig.getMinimumCoordinate());
			contourPlotterConfig.setMaximumCoordinate(zRangeConfig.getMaximumCoordinate());
		}

		double lineWidth = contourPlotterConfig.getLineWidth();
		double intervalCount = contourPlotterConfig.getDivisionCount();
		double minCoord = contourPlotterConfig.getMinimumCoordinate().doubleValue();
		double maxCoord = contourPlotterConfig.getMaximumCoordinate().doubleValue();

		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// If "With Surfaces" option is enabled, draw the contour lines with the solid foreground color.
		if (this.model.config.getPlotterConfiguration().getSurfacePlotterConfiguration().isPlotterEnabled()) {
			drawingParameter.setAutoColorEnabled(false);
			drawingParameter.setColor(model.config.getColorConfiguration().getForegroundColor());
		}

		// Extract all coordinate points of the data series.
		double[][] xCoords = dataSeries.getXCoordinates();
		double[][] yCoords = dataSeries.getYCoordinates();
		double[][] zCoords = dataSeries.getZCoordinates();
		boolean[][] visibilities = dataSeries.getVisibilities();

		// Draw lines for the direction of the right-side dimension.
		int leftDimLength = xCoords.length;
		for (int iL=0; iL<leftDimLength - 1; iL++) {

			int rightDimLength = xCoords[iL].length;
			for (int iR=0; iR<rightDimLength - 1; iR++) {

				// Draw a line only when both of its edge points are set to visible.
				boolean isLineVisible = visibilities[iL][iR] && visibilities[iL][iR + 1];
				if (!isLineVisible) {
					continue;
				}

				// The coordinates of the edge point A:
				double xA = xCoords[iL][iR];
				double yA = yCoords[iL][iR];
				double zA = zCoords[iL][iR];

				// The coordinates of the edge point B:
				double xB = xCoords[iL][iR + 1];
				double yB = yCoords[iL][iR + 1];
				double zB = zCoords[iL][iR + 1];

				// The coordinates of the edge point C:
				double xC = xCoords[iL + 1][iR + 1];
				double yC = yCoords[iL + 1][iR + 1];
				double zC = zCoords[iL + 1][iR + 1];

				// The coordinates of the edge point D:
				double xD = xCoords[iL + 1][iR];
				double yD = yCoords[iL + 1][iR];
				double zD = zCoords[iL + 1][iR];

				// Draw a contour lines in this cell, for every Z levels.
				for (int iinterval=0; iinterval<intervalCount + 1; iinterval++) {
					double zLevel = minCoord + (maxCoord - minCoord) * iinterval / (double)intervalCount;
					this.plotContourOfCell(
						xA, yA, zA,
						xB, yB, zB,
						xC, yC, zC,
						xD, yD, zD,
						zLevel, lineWidth, false, drawingParameter
					);
				}
			}
		}
	}


	/**
	 * Called when the currently requested plotting/re-plotting has been canceled.
	 *
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingCanceled(RinearnGraph3DPlottingEvent event) {
	}


	/**
	 * Called when the currently requested plotting/re-plotting has completed.
	 *
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingFinished(RinearnGraph3DPlottingEvent event) {
	}


	private void plotContourOfCell(
			double xA, double yA, double zA,
			double xB, double yB, double zB,
			double xC, double yC, double zC,
			double xD, double yD, double zD,
			double contourZ, double lineWidth, boolean isFlatEnabled, RinearnGraph3DDrawingParameter drawingParam){

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//
		// 現状のコードは、5.x からの単純移植であり、
		// 5.x でも既に過去版（多分Ver.3か4あたり）からの単純移植であるため相当汚いスパゲッティコードである。
		// とりあえず現状では機能実装を急ぐためそのままにしている。
		// 動くがそのうち綺麗にすべし。
		//
		// This code is a simple port from 5.x,
		// and even in 5.x, it was already a simple port from an older version (probably around Ver.3 or 4),
		// so it's a pretty messy spaghetti code.
		//
		// For now, leaving it as is to prioritize feature implementation.
		// It works, but should be cleaned up someday.
		//
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		double offsetMultiplier = 1.42; // 遠近オフセット調整用の係数。メッシュ四角形の最長辺を2個使った正方形の対角線の長さがオフセット値になるようにした値（√2）。どの角度から見ても必ず手前になる性質は保ちつつ短めに。

		int contourN = 0; // たぶん等高線の頂点数、線を引くアルゴリズムの中で加算していく
		double contourOffset = 0.0; // 描画の遠近オフセット量

		// たぶん四角形の点の位置ベクトル。名前のcontourってのはグローバルに配列確保する用の競合防止なので、別に等高線そのもののベクトルではない。
		double[] contourVx = {xA, xB, xC, xD, xA};
		double[] contourVy = {yA, yB, yC, yD, yA};
		double[] contourVz = {zA, zB, zC, zD, zA};

		// こっちが等高線のベクトル。上の四角形に対して求めた等高線の頂点座標を格納する。
		double[] contourCx = new double[8]; // [4] だと足りない場合がある。1辺の中で等高線との交差点を探す際に、線が水平だと等高線と一致するので、線の両端の2点登録する必要がある。
		double[] contourCy = new double[8]; // [4] だと足りない場合がある
		double[] contourCz = new double[8]; // [4] だと足りない場合がある

		// メッシュのちょうど境界に等高線が引かれる場合、丸め誤差の乗り方によっては、隣接するどっちのメッシュにも等高線が引かれない可能性がある
		// → ここはもう各次元の長さが±1のオーダーに正規化された長さの空間だから、誤差が絶対的に一定以内なら含めてしまっていいのでは。巨大数値とか極小数値とかは考えなくてもいい。
		double roundingError = 1.0E-10; // Z範囲からの超過がこの誤差以内なら、Z範囲内に入っていると見なす（メッシュ境界と等高線が重なる場合の丸め誤差対策）

		// 完全に等高線と同じ面を、フラットな面として塗りつぶすかどうか
		boolean fillFlatPlane = true;

		// 四角形の頂点のうち、等高線高さと同じ高さの点の数。フラットなメッシュに対しては描き方を変えるので、その判定に必要。3角形の場合はちょっと特殊なので、数を知りたいので int。
		int flatVerticesN = 0;
		for( int i=0; i<4; i++ ){
			if (contourVz[i] == contourZ) {
				flatVerticesN++;
			}
		}

		// 四角形の4つの辺にそれぞれ注目していく / まずオフセット求める用
		for(int i=0; i<4; i++){

			// 注目している辺の、高いほうの点と低い方の点のZ値を求める
			double contourMaxZ = Math.max( contourVz[i], contourVz[i+1] ); // 高い方の点のZ値
			double contourMinZ = Math.min( contourVz[i], contourVz[i+1] ); // 低い方の点のZ値

			// 低いほうと高いほうの点のZ値の間に contourZ があるなら、この辺を等高線が横切るはず
			if( contourMinZ-roundingError <= contourZ && contourZ <= contourMaxZ+roundingError ){

				// 四角形の辺のベクトルかな（点 i から点 i+1 を見る）
				double contourLx = contourVx[i+1] - contourVx[i];
				double contourLy = contourVy[i+1] - contourVy[i];
				double contourLz = contourVz[i+1] - contourVz[i];

				// Math.max( contourOffset ... があるから、辺を4つ辿る過程で、現在値を大きければ上書きしていってる。
				// 結局、四角形の辺の長さの最大値を求めて、それをオフセット値にしてる。なので、異様に長い四角形（勾配が急な所のメッシュなど）だと手前補正が強くなる。
				// -> Z方向の等高線にはZ方向の長さの加味いらないんじゃね。横から見るんだし。
				// -> いや、それないと上から見た時消えるよ
				contourOffset = Math.max( contourOffset, Math.max( Math.abs(contourLx), Math.max(Math.abs(contourLy),Math.abs(contourLz)) ) ); // もうちょっと距離オフセットの精度上げたいけど…
			}
		}

		// オフセット値の味付け
		contourOffset *= 2.0;
		if (isFlatEnabled) { // Flat時は綺麗に等高線を上に描きたいので、めっちゃ大きい値をオフセットしとく。どうせ一方向視点固定なので、常に上に描かれればそれでいいので。
			contourOffset += 100.0;
		}

		// 精度のため、3～4頂点が等高線と同じ高さの場合は頂点値をそのまま代入する。この場合は辺の中間に等高線の端点が来る事はありえないので。
		if (3 <= flatVerticesN) {
			for( int i=0; i<4; i++ ){
				if (contourVz[i] == contourZ) {
					contourCx[ contourN ] = contourVx[i];
					contourCy[ contourN ] = contourVy[i];
					contourCz[ contourN ] = contourVz[i];
					contourN++;
				}
			}

		// 一般化アルゴリズムで、辺の傾きから交点（等高線の端点）を求める必要がある場合
		} else {

			// 四角形の4つの辺にそれぞれ注目していく / メイン部（オフセット求めるのを上に切り出した）
			for( int i=0; i<4; i++ ){

				// 注目している辺の、高いほうの点と低い方の点のZ値を求める
				double contourMaxZ = Math.max( contourVz[i], contourVz[i+1] ); // 高い方の点のZ値
				double contourMinZ = Math.min( contourVz[i], contourVz[i+1] ); // 低い方の点のZ値

				// 低いほうと高いほうの点のZ値の間に contourZ があるなら、この辺を等高線が横切るはず
				if( contourMinZ-roundingError <= contourZ && contourZ <= contourMaxZ+roundingError ){

					// 四角形の辺のベクトルかな（点 i から点 i+1 を見る）
					double contourLx = contourVx[i+1] - contourVx[i];
					double contourLy = contourVy[i+1] - contourVy[i];
					double contourLz = contourVz[i+1] - contourVz[i];

					// 辺が水平でない場合
					if(  contourLz != 0 ){
						double rate = Math.abs( (contourZ-contourVz[i])/contourLz );

						// 以下、if外にあったのを中に入れた。後の else は完結処理になったので。

						// 等高線の頂点に登録し、線の本数をインクリメント
						contourCx[ contourN ] = contourVx[i] + contourLx*rate;
						contourCy[ contourN ] = contourVy[i] + contourLy*rate;
						contourCz[ contourN ] = contourVz[i] + contourLz*rate;
						contourN++;

					// 辺が水平な場合 ->
					// そうしないと、contourCx とかが未設定のまま後続処理で参照する事になってしまう。
					} else {

						// 辺そのものに一致する等高線を登録（即ち頂点2個を一気に登録）し、線の本数をインクリメント
						contourCx[ contourN ] = contourVx[i];
						contourCy[ contourN ] = contourVy[i];
						contourCz[ contourN ] = contourVz[i];
						contourN++;
						// ※ ここで drawLine とかして即描いて終わりにせず、一旦ちゃんと登録しないと、contourCx とかが未設定のまま後続処理で参照する事になってしまう。
					}
				}
			}
		}

		// 描画パラメータに遠近オフセット量を設定
		drawingParam.setDepthOffsetAmounts(-contourOffset*offsetMultiplier, -contourOffset*offsetMultiplier, -contourOffset*offsetMultiplier);

		// 普通に四角形の2辺のそれぞれの中間を等高線が横切る場合
		if( contourN == 2 ){
			this.renderer.drawLine(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], lineWidth, drawingParam);

		// 等高線が四角形の1つの辺の中と、1つの頂点を通る場合は、当たり判定的に頂点が2交点にダブルカウント（縮退）されて、合計3交点になる
		} else if( contourN == 3 ){

			// 元メッシュの3点が完全に等高線高さと一致する面は、塗りつぶすか何もしないかをオプションで選択
			if (flatVerticesN == 3) {

				if (fillFlatPlane) {
					this.renderer.drawTriangle(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], contourCx[2],contourCy[2],contourCz[2], drawingParam);

				} else {

					// Wire triangle
					this.renderer.drawLine(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], lineWidth, drawingParam);
					this.renderer.drawLine(contourCx[1],contourCy[1],contourCz[1], contourCx[2],contourCy[2],contourCz[2], lineWidth, drawingParam);
					this.renderer.drawLine(contourCx[2],contourCy[2],contourCz[2], contourCx[0],contourCy[0],contourCz[0], lineWidth, drawingParam);

					// -> 3角形の場合は4角形と違って、この場合でもどれかの点が高さ外なので、そこで折れ線があるべきで、なのでその点を挟む点間で線を引くべき？
					// -> とりあえず今は暫定的に閉多角形を描いておく
				}

			// それ以外は閉多角形を描く
			} else {

				// Wire triangle
				this.renderer.drawLine(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], lineWidth, drawingParam);
				this.renderer.drawLine(contourCx[1],contourCy[1],contourCz[1], contourCx[2],contourCy[2],contourCz[2], lineWidth, drawingParam);
				this.renderer.drawLine(contourCx[2],contourCy[2],contourCz[2], contourCx[3],contourCy[3],contourCz[3], lineWidth, drawingParam);
			}

		// 対角線で折った折り紙みたいな四角形の場合は、等高線が4辺の中をそれぞれ通って、4点を結ぶ場合があり得る。
		// または、四角形の2頂点を等高線が結ぶ、つまり対角線＝等高線となる場合も、当たり判定的には1頂点が2交点にダブルカウント（縮退）されるので、この処理になる
		// -> いまは4辺が等しい場合もこっち通るようにした。
		}else if( contourN == 4 ){

			// この場合に描くかどうかはサブオプション化すべき。山のふもとなら、山方向に隣接ポリゴンに対する処理で、接する辺（ふもとの立ち上がり始めの境界線）は塗られるわけだし。
			// 4辺等しいってのは、山のふもとの線ではなく平面を塗るかどうかって効き方をする。ふもとの境界線が描かれない場合はここじゃなく他の処理がおかしい。
			// なので、描くならぬりつぶしにすべきだし、そうじゃなければ何も描くべきではない。
			// そしてどっちがいいかはユーザー次第なのでサブオプション化すべき。

			// 元メッシュの4点が完全に等高線高さと一致する面は、塗りつぶすか何もしないかをオプションで選択
			if (flatVerticesN == 4) {
				if (fillFlatPlane) {
					this.renderer.drawQuadrangle(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], contourCx[2],contourCy[2],contourCz[2], contourCx[3],contourCy[3],contourCz[3], drawingParam);
				}

			// それ以外は閉多角形を描く
			} else {

				// Wire quadrangle
				this.renderer.drawLine(contourCx[0],contourCy[0],contourCz[0], contourCx[1],contourCy[1],contourCz[1], lineWidth, drawingParam);
				this.renderer.drawLine(contourCx[1],contourCy[1],contourCz[1], contourCx[2],contourCy[2],contourCz[2], lineWidth, drawingParam);
				this.renderer.drawLine(contourCx[2],contourCy[2],contourCz[2], contourCx[3],contourCy[3],contourCz[3], lineWidth, drawingParam);
				this.renderer.drawLine(contourCx[3],contourCy[3],contourCz[3], contourCx[0],contourCy[0],contourCz[0], lineWidth, drawingParam);
			}
		}
	}


}
