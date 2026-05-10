---
title: "Google Antigravityでブラウザ操作ができなくなった時の解決策（Chrome 147チルダ展開バグ）"
tags: ["Antigravity", "AI", "Chrome", "トラブルシューティング"]
---

最近、Googleが提供しているエージェント型IDE「Google Antigravity」で、ブラウザを開くことはできるのに**その後のブラウザ操作（クリックや検証など）が全く機能しなくなる**という現象に遭遇しました。

原因と回避策を特定したので共有します。Antigravityを使用している方で同様の症状に悩まされている方の参考になれば幸いです。

## 💻 マシンスペック / 環境

*   **マシン:** MacBook Pro (Apple M1)
*   **OS:** macOS
*   **メモリ:** 8 GB
*   **Chrome バージョン:** 147.0.7727.138
*   **Antigravity バージョン:** 1.23.2 (2026年4月版)

## ⚠️ 現象

Antigravityに「ブラウザを開いて特定のページを確認して」と指示すると、以下の現象が起きます。

1. Chromeのウィンドウは実際に立ち上がる。
2. しかし、Antigravity側ではタイムアウトになり、**「ブラウザを操作できない」** と返答される。
3. エラーログを確認すると、CDP (Chrome DevTools Protocol) のポート接続拒否エラーが出ている。
    ```text
    failed to connect to browser via CDP: http://127.0.0.1:9222
    CDP port not responsive in 5s: playwright: connect ECONNREFUSED 127.0.0.1:9222
    ```
4. 起動時にChrome側に **「Google Chrome はこのデータ ディレクトリへの読み書きを実行できません: ~/.gemini/antigravity-browser-profile」** という警告メッセージが表示される場合がある。

## 🕵️‍♂️ 原因

結論から言うと、**Chrome 147のアップデートにより、パス指定時のチルダ（`~`）展開の挙動が変わったこと**と、**Antigravity側のハードコードされた設定**が噛み合わなくなったことによるバグです。

Antigravityはブラウザ操作時に、以下の引数で専用のChromeインスタンスを立ち上げます。
```bash
--remote-debugging-port=9222
--user-data-dir=~/.gemini/antigravity-browser-profile
```

しかし、Chrome側が `~`（チルダ）をホームディレクトリとして展開できず、プロファイルへのアクセスエラーが発生します。
その結果、Chromeは指定されたオプションを破棄し、**通常のプロファイルで単なる新しいウィンドウとしてフォールバック起動してしまいます。**

これによって `--remote-debugging-port=9222` の指定も無視されるため、Antigravityがブラウザと通信する手段（CDPポート）がリッスンされず、操作不能に陥ります。

## 💡 解決策

根本的な解決はAntigravityの今後のアップデート（絶対パスでの指定への修正）を待つしかありません。
しかし、**絶対パスを用いて手動で事前にChromeを立ち上げておくワークアラウンド（回避策）** を使うことで、今すぐ正常にブラウザ操作ができるようになります。

### 手順：回避用起動スクリプトの作成

任意の場所（ワークスペースの直下など）に `start_browser.sh` というファイルを作成し、以下のコードを貼り付けます。

```bash:start_browser.sh
#!/bin/bash
# Antigravity ブラウザ操作復旧スクリプト

echo "🔄 既存のChromeを終了しています..."
osascript -e 'tell application "Google Chrome" to quit'
pkill -f "Google Chrome" 2>/dev/null
sleep 2

echo "🚀 Antigravity用Chromeを起動します..."
echo "✅ Antigravity側でブラウザ操作が可能になります。"
echo "終了するには Ctrl+C を押してください。"

# 絶対パスを使用してチルダ展開バグを回避
/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome \
  --remote-debugging-port=9222 \
  --user-data-dir=/Users/{ご自身のユーザー名}/.gemini/antigravity-browser-profile \
  --disable-fre \
  --no-default-browser-check \
  --no-first-run \
  --auto-accept-browser-signin-for-tests \
  --ash-no-nudges \
  --disable-features=OfferMigrationToDiceUsers,OptGuideOnDeviceModel \
  2>/dev/null
```

### 使い方

1. ターミナルで実行権限を付与します。
   ```bash
   chmod +x start_browser.sh
   ```
2. スクリプトを実行します。
   ```bash
   ./start_browser.sh
   ```
3. Chromeが立ち上がり、ターミナルが待機状態になります。
4. **この状態で、Antigravityにブラウザ操作の指示を出してください。** 
   既にポート9222が正しく開いているため、Antigravityがブラウザを正常にコントロールできるようになります。
5. 作業が終わったら、ターミナルで `Ctrl+C` を押してスクリプトを終了させます。

---
同じ問題で詰まっている方の助けになれば嬉しいです！
