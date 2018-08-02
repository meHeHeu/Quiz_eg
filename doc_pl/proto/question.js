/*
*	
*	Description: Quiz prototype. Question.
*	by: Michał Kloc
*
*/

var
	// general
	wsize   = {x: 1080,  y: 1920},
	wmiddle = {x: 0.5*wsize.x,  y: 0.5*wsize.y},
	wcolor = "#ffffff",
	// score
	ssize     = {x: 0.75*wsize.x,  y: 0.125*wsize.y},
	spos      = {x: 0.015625*wsize.x,  y: 0.03125*wsize.y},
	scolor    = "#444444",
	sfontsize = 20,
	stext     = "Wynik: 0/∞",
	// question
	qsize     = {x: 0.875*wsize.x,  y: 0.125*wsize.y},
	qpos      = {x: wmiddle.x-qsize.x/2,  y: spos.y+ssize.y},
	qcolor    = "#444444",
	qfontsize = 30,
	qtext     = "Najwybitniejszy polityk, to:",
	// toasts
	gatext = "dobrze",
	batext = "Tak. Zali każdy wie że to Jarosław. Błogosławione łono, czy coś tam.",
	// buttons
	bsize      = {x: 0.75*wsize.x,  y: (0.0625 + 0.03125)*wsize.y},
	bstartposy = 0.125*wsize.y,
	bcount     = 5,
	bpadding   = -9,
	blabels    = ["Jarosław", "Jarosław", "Jarosław",
		"Jarosław"];

function buttonPos(i) {
	return {
		x: wmiddle.x-bsize.x/2,
		y: wsize.y - bstartposy - (bcount - i) * (bsize.y + bpadding)
	};
}

function onGood() {
	device.vibrate(500);
	ui.toast(gatext);
}

function onBad() {
	ui.toast(batext);
}

/* -------------------------------------------------------------------------- */

ui.allowScroll(true);
ui.backgroundColor(wcolor);

ui.toolbar.show(false);

var score = ui.addText(stext, spos.x, spos.y, ssize.x, ssize.y);
score.textSize(sfontsize);
score.color(scolor);

var question = ui.addText(qtext, qpos.x, qpos.y, qsize.x, qsize.y);
question.textSize(qfontsize);
question.color(qcolor);

for(var i=0; i<bcount; ++i) {
	var
		label = blabels[i],
		pos = buttonPos(i),
		fun;

	if(label === undefined) {
	    label = "Głupie pytanie.";
	    fun = onBad;
	} else {
	    fun = onGood;
	}

	ui.addButton(label, pos.x, pos.y, bsize.x, bsize.y)
	  .onClick(fun);
}

