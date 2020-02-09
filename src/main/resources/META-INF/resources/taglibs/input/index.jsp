
<%@ include file="../common.jsp" %>

<!-- TODO show attributes from Hello taglib -->
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet"
	href="${taglibContextPath}/taglibs/input/main.css">

<div class="form-group input-text-wrapper ${dpTagRequired eq 'true' or dpTagRequired ne null  ? 'dpco-required' : ''}">
	<label class="control-label">
		<liferay-ui:message key="first-name" />: 
	</label>
	<input type="${dpTagType eq null ? 'text': dpTagType}"
		name="<portlet:namespace/>${dpTagName}"
		value="${fn:escapeXml(dpTagValue eq null ? '' : dpTagValue)}" />
</div>


<script src="${taglibContextPath}/taglibs/input/main.js"></script>