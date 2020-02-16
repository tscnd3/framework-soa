package com.xinyue.framework;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@JobRunner4TaskTracker
public class JobRunnerImpl implements JobRunner , ApplicationContextAware {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunnerImpl.class);
    
    private ApplicationContext applicationContext;

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();
            // TODO 业务逻辑
            LOGGER.info("我要执行：" + jobContext);
            // 会发送到 LTS (JobTracker上)
            bizLogger.info("测试，业务日志啊啊啊啊啊");
            
            Job job = jobContext.getJob();
            Map<String, String> map = job.getExtParams();
            String interf = map.get("interface");
            String methodStr = map.get("method");
            if (StringUtils.isNotEmpty(interf) && StringUtils.isNotEmpty(methodStr)) {
            	Object obj= applicationContext.getBean(Class.forName(interf));
                Method m = null;
                try {
                    m = obj.getClass().getDeclaredMethod(methodStr, Map.class);
                } catch (Exception e) {
                    LOGGER.info("Map 方法不存在");
                }
                if (m != null) {
                    m.invoke(obj, map);
                } else {
                    m = obj.getClass().getDeclaredMethod(methodStr);
                    m.invoke(obj);
                }
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}
}
