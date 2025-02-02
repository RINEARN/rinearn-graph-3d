package com.rinearn.graph3d.config.scale;

/**
 * The enum representing each mode to control the visibility of the scales.
 */
public enum ScaleVisibilityMode {

	/** Switches the visibility automatically, depending on the angle of the graph.  */
	AUTO,

	/** Always keeps the scale visible.  */
	ALWAYS_VISIBLE,
	// !!!!! NOTE !!!!!  ちょっと冗長すぎる？ VISIBLE だけでいい？
	// -> もし後々でモードが増えていった時に、単に VISIBLE/INVISIBLE では微妙になる可能性もあるが…それでも分かるかな？
	//    -> 逆に微妙になるパターンを思い浮かぶかどうか。現実的にありそうなやつで。
	//       -> まあ微妙っちゃ AUTO がある時点で既に VISIBLE/INVISIBLE は微妙でもある。
	//          これら3つだけなら、VISIBLE/INVISIBLE の切り替えを自動でやるのが AUTO って解釈でギリ許容でも、あと1つ生えると難解化するかも。

	/** Always keeps the scale invisible. */
	ALWAYS_INVISIBLE,
}
