
/*-------SUGGESTION PROVIDER IMPLEMENTATION------------*/


var xml_req = new XMLHttpRequest();

function SuggestionExtractor() {};

/*
 * Queries for the XML, and passes request to pullSuggestions.
 */
SuggestionExtractor.prototype.requestData = function(oAutoSuggestControl, bTypeAhead)
{
    var cur_query = encodeURI(oAutoSuggestControl.textbox.value);
    var requestURL = "/eBay/suggest?query=" + cur_query;
    console.log(requestURL);
    xml_req.open("GET", requestURL, true);
    xml_req.onreadystatechange = this.pullSuggestions(oAutoSuggestControl, bTypeAhead);

    xml_req.send(null);
};


/*
 * Request suggestions for the given autosuggest control. 
 */
SuggestionExtractor.prototype.pullSuggestions = function(oAutoSuggestControl, bTypeAhead)
{
    return function() {
        var suggestArr = new Array();
        if(xml_req.readyState == 4)
        {
            //console.log("ReadyState==4");
            var suggestionsList = xml_req.responseXML.getElementsByTagName('CompleteSuggestion');
            for (var itr = 0; itr < suggestionsList.length ; itr++) 
            {
                
                //console.log(suggestions[itr].childNodes[0].getAttribute("data"));
                /*Add suggestion data to array as text.*/
                suggestArr.push(suggestionsList[itr].childNodes[0].getAttribute("data"));
            }
        }

        oAutoSuggestControl.autosuggest(suggestArr, bTypeAhead);
    }
};