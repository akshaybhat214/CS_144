/*Akshay Bhat and Aditya Padmakumar*/

/*-------SUGGESTION PROVIDER IMPLEMENTATION------------*/

var xml_req = new XMLHttpRequest();
function SuggestionExtractor() {}

SuggestionExtractor.prototype.requestData = function(oAutoSuggestControl, bTypeAhead)
{
	var cur_query = encodeURI(oAutoSuggestControl.textbox.value);
	var requestURL = "/eBay/suggest?query=" + cur_query;
	xml_req.open("GET", request);
	xml_req.onreadystatechange = this.pullSuggestions(oAutoSuggestControl, bTypeAhead);

	xml_req.send(null);
};

SuggestionExtractor.prototype.pullSuggestions = function(oAutoSuggestControl, bTypeAhead)
{
	var cur_query = encodeURI(oAutoSuggestControl.textbox.value);
	var requestURL = "/eBay/suggest?query=" + cur_query;
	xml_req.open("GET", request);
	var suggestArr = [];
	xml_req.onreadystatechange = function()
	{
		if(xml_req.readyState == 4)
		{
			var suggestion = xml_req.responseXML.getElementsByTagName('CompleteSuggestion');
			for (var itr = 0; itr < suggestions.length ; itr++) 
			{
				//Add suggestion data to array as text.
    			suggestArr.push(suggestions[i].childNodes[0].getAttribute("data"));
			}
		}

		oAutoSuggestControl.autosuggest(suggestArr, bTypeAhead);
	}

	xml_req.send(null);
};

/*--------AUTOSUGGEST BOX IMPLEMENTATION---------------*/

/*The control class for the textbox*/
function AutoSuggestControl(oTextbox, oProvider) {
    this.provider = oProvider;
    this.textbox = oTextbox;
    //this.layer = null;
    this.init(); 
    //this.cur= -1;
};

/*Event handler for the textbox.*/
AutoSuggestControl.prototype.init = function () {

    var oThis = this;
    this.textbox.onkeyup = function (oEvent) {
        if (!oEvent) {
            oEvent = window.event;
        }
        oThis.handleKeyUp(oEvent);
    };

    this.textbox.onkeydown = function (oEvent) {
        if (!oEvent) {
            oEvent = window.event;
        }
        oThis.handleKeyDown(oEvent);
    };

    //need to handle other events....
};

/*Range selector for the textbox for the textbox IE/Mozilla */
AutoSuggestControl.prototype.selectRange = function (iStart, iLength) {
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", iStart); 
        oRange.moveEnd("character", iLength - this.textbox.value.length); 
        oRange.select();
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(iStart, iLength);
    }

    this.textbox.focus(); 
};

/*Selects autosuggested portion of suggestion in textbox*/
AutoSuggestControl.prototype.typeAhead = function (sSuggestion) {
    if (this.textbox.createTextRange || this.textbox.setSelectionRange) {
        var iLen = this.textbox.value.length; 
        this.textbox.value = sSuggestion; 
        this.selectRange(iLen, sSuggestion.length);
    }
};

/*Utilizes typeahead with the top-most suggestion*/
AutoSuggestControl.prototype.autosuggest = function (aSuggestions) {

    if (aSuggestions.length > 0) {
        this.typeAhead(aSuggestions[0]);
    }
};

/*Handler for keyUp, triggers the autosuggest() method*/
AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {
     var iKeyCode = oEvent.keyCode;

    if (iKeyCode == 8 || iKeyCode == 46) { //backspace/delete
        this.provider.requestData(this, false);
    }
     else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
        //ignore
    } else {
        this.provider.requestData(this);
    }
};

