@echo off

if not exist docs ( mkdir docs & echo mkdir...docs)

copy assignment-api\build\asciidoc\html5\index.html docs\assignment-api-document.html