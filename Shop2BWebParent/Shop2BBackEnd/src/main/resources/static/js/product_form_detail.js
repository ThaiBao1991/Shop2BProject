
$(document).ready(function () {
  $("a[name='linkRemoveDetail']").each(function(index){
    $(this).click(function(){
      removeDetailSectionByIndex(index)
    });
  });

});

function removeDetailSectionByIndex(index){
  $("#divDetail" +index).remove();
}

function addNextDetailSection(){
  allDivDetails = $("[id^='divDetail']");
  divDetailsCount = allDivDetails.length;

  htmlDetailSection = `
    <div class="form-inline" id="divDetail${divDetailsCount}">
    <input type="hidden" name="detailIDs" value="0">
    <label class="m-3" for="">Name :</label>
    <input type="text" class="form-control w-25" name="detailNames" maxlength="255">
    <label class="m-3" for="">Value :</label>
    <input type="text" class="form-control w-25" name="detailValues" maxlength="255">
    </div>
  `;
  
  $("#divProductDetails").append(htmlDetailSection);
  previousDivDetailSection = allDivDetails.last();
  previousDivDetailID = previousDivDetailSection.attr("id");


  htmlLinkRemove =`
    <a class="btn fas fa-times-circle fa-2x icon-dark" 
    title="Remove this detail" 
    href="javascript:removeDetailSectionById('${previousDivDetailID}')"></a>
    `;

  
  
  previousDivDetailSection.append(htmlLinkRemove);
  $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
  $("#" + id).remove();
}