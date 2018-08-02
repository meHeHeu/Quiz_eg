/*
*	
*	Description: Quiz prototype. Main menu.
*	by: Michał Kloc
*
*/

var
	// general
	wsize   = {x: 1080,  y: 1920},
	wmiddle = {x: 0.5*wsize.x,  y: 0.5*wsize.y},
	wcolor = "#ffffff",
	tcolor = {r: 55, g: 155, b: 155, a: 255},
	ttitle = "Quiz",
	// header
	hsize     = {x: 0.75*wsize.x,  y: 0.125*wsize.y},
	hpos      = {x: wmiddle.x-hsize.x/2,  y: 100},
	hcolor    = "#444444",
	hfontsize = 45,
	htext     = "Wybór quizu",
	// buttons
	bsize      = {x: 0.75*wsize.x,  y: (0.0625 + 0.03125)*wsize.y},
	bstartposy = hpos.y + hsize.y + 0.03125*wsize.y,
	bcount     = 5,
	bpadding   = -9,
	blabels    = ["Filmy Disneya", "Ziemniaki", "C++",
		"Wybitni polscy politycy"];

function buttonPos(i) {
	return {
		x: wmiddle.x-bsize.x/2,
		y: bstartposy + i * (bsize.y + bpadding)
	};
}

function onBClick() {
	device.vibrate(500);
}

/* -------------------------------------------------------------------------- */

ui.allowScroll(true);
ui.backgroundColor(wcolor);

ui.toolbar.title(ttitle);
ui.toolbar.bgColor(tcolor.r, tcolor.g, tcolor.b, tcolor.a);
ui.toolbar.show(true);

var header = ui.addText(htext, hpos.x, hpos.y, hsize.x, hsize.y);
header.textSize(hfontsize);
header.color(hcolor);

for(var i=0; i<bcount; ++i) {
	var
		label = blabels[i],
		pos   = buttonPos(i);
	label = label === undefined ? "Nyc" : label;

	ui.addButton(label, pos.x, pos.y, bsize.x, bsize.y)
	  .onClick(onBClick);
}

