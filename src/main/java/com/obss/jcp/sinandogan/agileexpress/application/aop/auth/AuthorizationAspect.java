package com.obss.jcp.sinandogan.agileexpress.application.aop.auth;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions.DontHavePermissionException;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@Aspect
public class AuthorizationAspect {
    private final HttpServletRequest request;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthorizationAspect(HttpServletRequest request, JwtService jwtService, UserService userService) {
        this.request = request;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Before("@annotation(com.obss.jcp.sinandogan.agileexpress.application.aop.auth.RolesAllowed)")
    void authorize(JoinPoint joinPoint) {
        boolean isAuthorized = false;
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();

        Role[] expectedRoles = ms.getMethod().getAnnotation(RolesAllowed.class).values();
        HashMap<Role, Boolean> expectedRolesMap = new HashMap<>();

        var expectedRolesList = Arrays.stream(expectedRoles).toList();
        for (Role expectedRole : expectedRolesList)
            expectedRolesMap.put(expectedRole, true);

        var jwt = request.getHeader("Authorization").substring(7);
        var email = jwtService.extractUsername(jwt);
        String title = userService.findUserByEmail(email).getTitle();

        if (expectedRolesMap.containsKey(Role.fromValue(title))) {
            isAuthorized = true;
        }
        if (!isAuthorized) {
            throw new DontHavePermissionException("You don't have permission to do this action.", email);
        }
    }

    //    @Before("@annotation(com.obss.jcp.sinandogan.agileexpress.application.aop.auth.RolesAllowed)")
//    void authorize(JoinPoint joinPoint) {
//        idIndex = 0;
//        boolean isAuthorized = false;
//        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
//        String[] parameterNames = ms.getParameterNames();
//        Object[] args = joinPoint.getArgs();
//
//        Role[] expectedRoles = ms.getMethod().getAnnotation(RolesAllowed.class).values();
//        HashMap<Role, Boolean> expectedRolesMap = new HashMap<>();
//
//        var expectedRolesList = Arrays.stream(expectedRoles).toList();
//        for (Role expectedRole : expectedRolesList)
//            expectedRolesMap.put(expectedRole, true);
//
//        var jwt = request.getHeader("Authorization").substring(7);
//        var email = jwtService.extractUsername(jwt);
//        String title = userService.findUserByEmail(email).getTitle();
//        if (title.equals("admin")) {
//            return;
//        }
//        Project project = getProject(args, parameterNames);
//        User user = userService.findUserByEmail(email);
//
//        List<Role> userRoles = new ArrayList<>();
//        if (Objects.equals(user.getTitle(), "admin")) {
//            userRoles.add(Role.ADMIN);
//        }
//        if (Objects.equals(project.getEmailOfManager(), email)) {
//            userRoles.add(Role.MANAGER);
//        }
//        if (Objects.equals(project.getEmailOfTeamLead(), email)) {
//            userRoles.add(Role.TEAM_LEAD);
//        }
//        if (project.getEmailsOfMembers().contains(email)) {
//            userRoles.add(Role.MEMBER);
//        }
//        for (Role userRole : userRoles) {
//            if (expectedRolesMap.containsKey(userRole)) {
//                isAuthorized = true;
//                break;
//            }
//        }
//        if (!isAuthorized) {
//            throw new RuntimeException("You don't have permission to do this action.");
//        }
//    }

//    private Project getProject(Object[] args, String[] parameterNames) {
//        UUID id = extractProjectContextIdFromArgs(args, parameterNames);
//        if (idIndex == 0) {
//            return projectService.getById(id);
//        }
//        if (idIndex == 1) {
//            return projectService.getProjectByTaskId(id);
//        }
//        if (idIndex == 2) {
//            return projectService.getProjectByBacklogId(id);
//        }
//        return projectService.getProjectBySprintId(id);
//    }
//
//    private UUID extractProjectContextIdFromArgs(Object[] args, String[] parameterNames) {
//        for (int i = 0; i < args.length; i++) {
//            UUID id = tryGetIdByParamName(args[i], parameterNames[i], "projectId");
//            if (id == null) id = tryGetIdByMethodName(args[i], "getProjectId");
//            if (id != null) return id;
//
//            id = tryGetIdByParamName(args[i], parameterNames[i], "taskId");
//            if (id == null) id = tryGetIdByMethodName(args[i], "getTaskId");
//            if (id != null) {
//                idIndex = 1;
//                return id;
//            }
//            id = tryGetIdByParamName(args[i], parameterNames[i], "backlogId");
//            if (id == null) id = tryGetIdByMethodName(args[i], "getBacklogId");
//            if (id != null) {
//                idIndex = 2;
//                return id;
//            }
//            id = tryGetIdByParamName(args[i], parameterNames[i], "sprintId");
//            if (id == null) id = tryGetIdByMethodName(args[i], "getSprintId");
//            if (id != null) {
//                idIndex = 3;
//                return id;
//            }
//
//        }
//        throw new RuntimeException("DTO içinde projectId, taskId veya backlogId bulunamadı");
//    }
//
//    private UUID tryGetIdByMethodName(Object arg, String methodName) {
//        try {
//            Method method = arg.getClass().getMethod(methodName);
//            Object result = method.invoke(arg);
//            if (result instanceof UUID uuid) {
//                return uuid;
//            }
//        } catch (NoSuchMethodException ignored) {
//        } catch (Exception e) {
//            throw new RuntimeException("ID alınırken hata oluştu: " + methodName, e);
//        }
//        return null;
//    }
//
//    private UUID tryGetIdByParamName(Object arg, String parameterName, String targetParamName) {
//        if (parameterName.equals(targetParamName) && arg instanceof UUID uuid) {
//            return uuid;
//        }
//        return null;
//    }
}
