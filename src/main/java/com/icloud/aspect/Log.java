package com.icloud.aspect;


import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 512162086@qq.com on 2018/8/22 .
 * 日志切面，切入需要加日志的方法
 */
@Aspect
@Component
public class Log {

    public final static Logger log = LoggerFactory.getLogger(Log.class);
//            （* com.evan.crm.service.*.*（..））中几个通配符的含义：
//            |第一个 * —— 通配 随便率性返回值类型|
//            |第二个 * —— 通配包com.evan.crm.service下的随便率性class|
//            |第三个 * —— 通配包com.evan.crm.service下的随便率性class的随便率性办法|
//            |第四个 .. —— 通配 办法可以有0个或多个参数|

//    @Pointcut("execution(* com.zhumeng.service.*.*(..))")
//    @Pointcut("execution(* com.icloud.web..*.*(..))")
    public void webLog(){}

//    @Before("webLog()")
    public void before(JoinPoint point) throws ClassNotFoundException,NotFoundException {
        String classType = point.getTarget().getClass().getName();
        log.info("classType==="+classType);
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = point.getSignature().getName(); //获取方法名称
        Object[] args = point.getArgs();//参数
        //获取参数名称和值
        Map<String,Object > nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName,args);
        log.info("methodName===："+methodName);
        log.info("参数名=值：："+nameAndArgs.toString());
//        log.info("@Before：参数为：" + Arrays.toString(point.getArgs()));
//        log.info("@Before：被织入的目标对象为：" + point.getTarget());
    }
//    @After("webLog()")
    public void after(){
        System.out.println("执行方法后");
    }

//    @AfterReturning("webLog()")
    public void afterReturning(){
        System.out.println("返回通知");
    }
//    @AfterThrowing("webLog()")
    public void AfterThrowing(){
        System.out.println("异常通知");
    }

//    @Around("webLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("环绕前");
        System.out.println("签名:"+pjp.getSignature());
        //执行目标方法
        Object proceed = pjp.proceed();
        System.out.println("环绕后");
        return proceed;
    }

    private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
        Map<String,Object > map=new HashMap<String,Object>();

        ClassPool pool = ClassPool.getDefault();
        //ClassClassPath classPath = new ClassClassPath(this.getClass());
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        // String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++){
            map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名
        }

        //Map<>
        return map;
    }


}
