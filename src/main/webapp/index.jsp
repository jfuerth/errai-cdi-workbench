<%
  final String queryString = request.getQueryString();
  final String redirectURL = "CdiWorkbench/Uberfire.html" + (queryString == null ? "" : "?" + queryString);
  response.sendRedirect(redirectURL);
%>
