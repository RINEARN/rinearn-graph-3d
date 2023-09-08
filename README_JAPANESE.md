# RINEARN Graph 3D / Ver. 6

( &raquo; [English README](./README.md) )

次期版リニアングラフ（Ver.6）を開発するためのリポジトリです。

なお、現行版（Ver.5.6）は以下のサイトからダウンロードできます：
<br />
[https://www.rinearn.com/graph3d/](https://www.rinearn.com/graph3d/)


## 必要なもの

* Java&reg; Development Kit (JDK), 17以降

## ビルド方法

ダウンロードや clone 後、とりあえず即席でビルドしたい場合のために、簡易的なビルドスクリプトが同梱されています。

Microsoft Windows&reg; をご使用の場合は「 build.bat 」を実行してください。その他のOSでは、以下のように「 build.sh 」を実行してください：

    cd <this_directory>
    sudo chmod +x ./build.sh
    ./build.sh

定常的にビルドする場合は、上記スクリプトの内容を参考に、ご使用のビルドツールやIDEに合わせて設定を構築してください。


## 実行方法

まだ開発の途中であるため、きちんとしたアプリケーションとしての起動方法は準備されていません。下記は開発作業のための実行方法です。

Microsoft Windows&reg; をご使用の場合は「 run.bat 」を実行してください。その他のOSでは、以下のように「 run.sh 」を実行してください：

    cd <this_directory>
    sudo chmod +x ./run.sh
    ./run.sh

どういう画面が起動されるかや、その上に何が表示されるかは、その時々に開発している内容によって異なります。

## API仕様

グラフ描画ライブラリとして使う場合、Ver.6 では、基本的に Ver.5.6 とAPI互換が保たれる予定です。
Ver.6のAPI仕様書はまだ用意されていませんが、以下から Ver.5.6 の仕様書を参照できます：

* [API仕様書 日本語版](https://www.rinearn.com/ja-jp/graph3d/api/)
* [API仕様書 英語版](https://www.rinearn.com/en-us/graph3d/api/)

上記の仕様書にリストアップされている機能のうち、大半は Ver.6 ではまだ実装されていない事にご留意ください。これから段階的に実装が進んでいきます。

なお、上記APIを用いて、リニアングラフ3Dをグラフ描画ライブラリとして使う方法については、以下の記事で解説しています：

* [Java言語での制御・自動処理と直接的な3D描画 - リニアングラフ3D ユーザーガイド](https://www.rinearn.com/ja-jp/graph3d/guide/api)


## 開発元について

リニアングラフ3Dは、日本のソフトウェア開発スタジオ [RINEARN](https://www.rinearn.com/) が開発しています。作者は松井文宏です。
ご質問やフィードバックなどをお持ちの方は、ぜひ御気軽にどうぞ。

## 参考情報

このリポジトリで開発中の、次期版リニアングラフ3D（Ver.6）についての情報をもっと知りたい場合は、以下のウェブサイトや記事などが参考になるかもしれません。

<dl>
    <dt style="margin-left: 40px; display: list-item; list-style-type: disc; font-weight: normal; font-style: normal;">
        <a href="https://www.rinearn.com/ja-jp/info/news/2023/0904-software-update">リニアングラフ3Dをアップデート、次期版（Ver.6）の開発もスタート! - RINEARN お知らせ記事 / 2023年9月4日</a>
    </dt>
    <dd style="margin-left: 60px;">
        記事の後半で、Ver.6 の開発コンセプトなどを紹介しています。
    </dd>
</dl>


---

### 本文中の商標などについて

- OracleとJavaは、Oracle Corporation 及びその子会社、関連会社の米国及びその他の国における登録商標です。文中の社名、商品名等は各社の商標または登録商標である場合があります。

- Microsoft Windows は米国 Microsoft Corporation の米国およびその他の国における登録商標です。

- その他、文中に使用されている商標は、その商標を保持する各社の各国における商標または登録商標です。
