<#macro ks_applicationHeader element>

<script type="text/javascript">
    function logout() {
        var url = "${ConfigProperties['rice.server.url']}/backdoorlogin.do?methodToCall=logout";
        redirect(url);
    }
</script>

<div class="ks-uif-viewHeader-container navbar-inverse navbar">
    <img class="ks-logo-image" title="Kuali Student" src="../themes/ksboot/images/header/logo_kuali.png">
    <span class="ks-header-student">Student</span>

    <div class="header-right-group">
        <ul class="ks-header-list nav pull-right">
            <li class="ks-header-action-list"><a href="${ConfigProperties['kew.url']}/ActionList.do">Action List</a>
            </li>

            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="icon-user icon-white"></i>
                ${UserSession.loggedInUserPrincipalName!"You are not logged in."}
                  <b class="caret"></b>
              </a>
              <ul class="dropdown-menu">
                  <li>
                      <a href="#" onclick="logout();">Logout</a>
                  </li>
              </ul>
          </li>
        </ul>
    </div>
</div>

</#macro>