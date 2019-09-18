<%@ page import="Com.SharePackages.Tools" %>
<%@ page import="Com.DBActionCom.DBMemCached" %>
<%
    String privilege=Tools.judgeUserMenuItemPrivilege(request);
    if("-2".equals(privilege)){
        DBMemCached.getInstance().initMenuAllPrivilege();
        privilege=Tools.judgeUserMenuItemPrivilege(request);
    }
    if("-1".equals(privilege)||"-2".equals(privilege)){
        response.sendRedirect("/sitehome.html");
        return;
    }
%>
<script type="text/javascript">
    function initPrivilege(privilege){
        if(isNaN(privilege)){
            var data=eval('('+privilege+')');
            for(var key in data){
                if(data[key]=="0"){
                    var keyElem=document.getElementById(key);
                    if(typeof keyElem!="undefined"&&keyElem!=null){
//                      keyElem.parentNode.removeChild(keyElem);
                        keyElem.style.display="none";
                    }
                }
            }
        }
    }
</script>
<%
    out.print("<script type=\"text/javascript\">initPrivilege('"+privilege+"');</script>");
%>