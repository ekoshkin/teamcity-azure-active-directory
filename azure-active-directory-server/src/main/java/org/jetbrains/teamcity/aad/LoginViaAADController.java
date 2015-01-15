package org.jetbrains.teamcity.aad;

import jetbrains.buildServer.controllers.AuthorizationInterceptor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.controllers.login.WebLoginModel;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Evgeniy.Koshkin
 */
public class LoginViaAADController extends BaseController {

  @NotNull public static final String LOGIN_PATH = "/aadLogin.html";
  @NotNull private final WebLoginModel myWebLoginModel;

  public LoginViaAADController(@NotNull final WebControllerManager webManager,
                               @NotNull WebLoginModel webLoginModel,
                               @NotNull AuthorizationInterceptor authInterceptor) {
    myWebLoginModel = webLoginModel;
    webManager.registerController(LOGIN_PATH, this);
    authInterceptor.addPathNotRequiringAuth(LOGIN_PATH);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    if (SessionUser.getUser(request) == null) {
      return redirect(myWebLoginModel.getLoginPageUrl(request));
    }
    return redirect(request.getContextPath() + "/overview.html");
  }

  @NotNull
  private static ModelAndView redirect(@NotNull final String url) {
    return new ModelAndView(new RedirectView(url));
  }
}
