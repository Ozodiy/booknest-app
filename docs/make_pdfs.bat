@echo off
REM One-click: rebuild the three report PDFs from the Markdown sources.
REM Run from anywhere; paths are resolved relative to this file.
setlocal
cd /d "%~dp0"

echo [1/2] Converting Markdown reports to HTML...
python build_pdfs.py || goto :err

echo [2/2] Printing HTML to PDF via Chrome headless...
set "CHROME=C:\Program Files\Google\Chrome\Application\chrome.exe"
if not exist "%CHROME%" set "CHROME=C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"

for %%B in (BookNest_Lab2_Report BookNest_Lab3_Report BookNest_Final_Report) do (
    "%CHROME%" --headless --disable-gpu --no-pdf-header-footer ^
        --print-to-pdf="%cd%\%%B.pdf" "%cd%\%%B.html"
    echo   wrote %%B.pdf
)

echo Done. PDFs are in %cd%
goto :eof

:err
echo Build failed. Make sure Python and the 'markdown' package are installed:
echo     python -m pip install markdown
exit /b 1
