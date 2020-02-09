<%@ include file="../common.jsp"%>


	<div class="dpco-captcha-container">
		<div class="row">
			<div class="col-sm-6">
				<div class="dpco-form-group">
					<label class="dpco-control-label">
						<liferay-ui:message key="captcha" />: 
					</label>
					<div class="dpco-captcha-holder">
				   		<input name="<portlet:namespace/>captcha" type="text" class="dpco-form-control dpco-captcah-input text-left" 
				   		data-required-error-message="captcha-is-required" 
				   		autocomplete="false"
				   		/>
						<img id="captchaImg" src="" class="captcha-img" alt="captcha" height="45" />
					    <button type="button" id="refreshCaptcha" class="dpco-captcha-btn" data-captcha-url="/o/captcha-refresh">
					    	<i class="zmdi zmdi-refresh"></i>
					    </button>
				     </div>
				</div>
			</div>
		</div>
	</div>
	
	
<script>

reCaptcha("#refreshCaptcha");

$("#refreshCaptcha").click(function(){
	reCaptcha(this);
});

function reCaptcha(ele){
	var captchaURL = $(ele).data("captcha-url");
	
	if(!captchaURL.length)
		return false;
	
	jQuery.ajax({
		url : captchaURL,
		success : function(result) {
			$("#captchaImg").attr("src", result)
		}
	});	
}

</script>	