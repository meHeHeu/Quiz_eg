all: dokumentacja.pdf

dokumentacja.pdf: dokumentacja.tex
	pdflatex dokumentacja.tex
	pdflatex dokumentacja.tex

.PHONY: clean
clean:
	rm -rf dokumentacja.aux dokumentacja.log dokumentacja.out dokumentacja.toc dokumentacja.pdf

.PHONY: partial_clean
partial_clean:
	rm -rf dokumentacja.aux dokumentacja.log dokumentacja.out dokumentacja.toc

