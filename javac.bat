@echo off
@del /s /q *.class
"C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe" -d ./bin --module-path ./lib/ --add-modules=javafx.controls,javafx.fxml --source-path ./src %*