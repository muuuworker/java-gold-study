#!/bin/bash
# Antigravity ブラウザ操作復旧スクリプト
# 
# 使い方:
# 1. ./start_browser.sh を実行
# 2. Antigravityでブラウザ操作を行う（操作可能になります）
# 3. 終わったらターミナルで Ctrl+C して終了

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
  --user-data-dir=/Users/mtzk/.gemini/antigravity-browser-profile \
  --disable-fre \
  --no-default-browser-check \
  --no-first-run \
  --auto-accept-browser-signin-for-tests \
  --ash-no-nudges \
  --disable-features=OfferMigrationToDiceUsers,OptGuideOnDeviceModel \
  2>/dev/null
