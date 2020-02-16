package com.xinyue.framework;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.RetryJobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import com.github.ltsopensource.jobclient.support.JobCompletedHandler;

@Component
public class LtsClient{
	
    private JobClient<?, ?> jobClient;
    
    @Value("${lts.jobclient.cluster-name}")
    private String clusterName;
    
    @Value("${lts.jobclient.registry-address}")
    private String registryAddress;

    @Value("${lts.jobclient.node-group}")
    private String nodeGroup;

    private JobCompletedHandler jobCompletedHandler;
    
    @PostConstruct
    public void start() {
        jobClient = new RetryJobClient();
        jobClient.setNodeGroup(nodeGroup);
        jobClient.setClusterName(clusterName);
        jobClient.setRegistryAddress(registryAddress);
        if (jobCompletedHandler == null) {
            jobCompletedHandler = new JobCompletedHandlerImpl();
        }
        jobClient.setJobCompletedHandler(jobCompletedHandler);
        jobClient.addConfig("job.fail.store", "mapdb");
        jobClient.start();
    }

    /**
     * cron 表达式任务
     * @param className 类全名（包名加类名）
     * @param method 方法名
     * @param parame 类型属性值对应的键值对
     * @param cornExpression 调度表达式　例：0 0/5 * * * ?
     * @return
     */
    public Result submitCronJob(String className, String method, Map<String, String> params, String cornExpression, String code,int maxRetryTimes) {
        if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(method) && StringUtils.isNotEmpty(cornExpression)) {
            Job job = new Job();
            Date date = new Date();
            int index = className.lastIndexOf(".");
            String jobId = className.substring(index + 1) + "_" + method;
            job.setTaskId(jobId);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("interface", className);
            paramMap.put("method", method);
            if (params != null && params.size() > 0) {
                paramMap.putAll(params);
            }
            job.setExtParams(paramMap);
            job.setTaskTrackerNodeGroup(nodeGroup); // 执行要执行该任务的taskTracker的节点组名称
            job.setNeedFeedback(true);
            job.setReplaceOnExist(true); // 当任务队列中存在这个任务的时候，是否替换更新
            job.setCronExpression(cornExpression);
            job.setTriggerDate(date);
            job.setMaxRetryTimes(maxRetryTimes);
            Response response = jobClient.submitJob(job);
            Result result = new Result();
            result.setJobId(jobId);
            result.setCode(response.getCode());
            result.setMsg(response.getMsg());
            result.setSuccess(response.isSuccess());
            return result;
        }
        return null;
    }

    /**
     * 重复任务(创建任务会马上执行一次，下次执行依赖上次执行时间)
     * @param className 类全名（包名加类名）
     * @param method 方法名
     * @param parame 类型属性值对应的键值对　例：{"java.lang.Integer_1":"-3"}
     * @param count  执行次数
     * @param interval 间隔时间单位是毫秒数
     * @param maxRetryTimes 最大重试次数
     * @return
     */
    public Result submitRepeatJob(String className, String method, Map<String, String> params, int count, long interval,int maxRetryTimes) {
        if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(method) && count > 0 && interval > 0) {
            Job job = new Job();
            Date date = new Date();
            int index = className.lastIndexOf(".");
            String jobId = className.substring(index + 1) + "_" + method + "_" + date.getTime();
            job.setTaskId(jobId);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("interface", className);
            paramMap.put("method", method);
            if (params != null && params.size() > 0) {
                paramMap.putAll(params);
            }
            job.setExtParams(paramMap);
            job.setTaskTrackerNodeGroup(nodeGroup);
            job.setNeedFeedback(true);
            job.setReplaceOnExist(true); // 当任务队列中存在这个任务的时候，是否替换更新
            job.setRepeatCount(count); // 一共执行50次
            job.setRepeatInterval(interval); // 50s 执行一次
            job.setMaxRetryTimes(maxRetryTimes);
            Response response = jobClient.submitJob(job);
            Result result = new Result();
            result.setJobId(jobId);
            result.setCode(response.getCode());
            result.setMsg(response.getMsg());
            result.setSuccess(response.isSuccess());
            return result;
        }
        return null;

    }

    /**
     * 立即执行
     * @param className  类全名（包名加类名）
     * @param method 方法名
     * @param parame 类型属性值对应的键值对　例：{"java.lang.Integer_1":"-3"}
     * @param maxRetryTimes 最大重试次数
     * @return
     */
    public Result submitRealtimeJob(String className, String method, Map<String, String> params,int maxRetryTimes) {
        Job job = new Job();
        Date date = new Date();
        int index = className.lastIndexOf(".");
        String jobId = className.substring(index + 1) + "_" + method + "_" + date.getTime();
        job.setTaskId(jobId);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("interface", className);
        paramMap.put("method", method);
        if (params != null && params.size() > 0) {
            paramMap.putAll(params);
        }
        job.setExtParams(paramMap);
        job.setTaskTrackerNodeGroup(nodeGroup);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true); // 当任务队列中存在这个任务的时候，是否替换更新
        job.setMaxRetryTimes(maxRetryTimes);
        Response response = jobClient.submitJob(job);
        Result result = new Result();
        result.setJobId(jobId);
        result.setCode(response.getCode());
        result.setMsg(response.getMsg());
        result.setSuccess(response.isSuccess());
        return result;
    }

    /**
     * 定时任务
     * @param className  类全名（包名加类名）
     * @param method 方法名
     * @param parame 类型属性值对应的键值对　例：{"sex":"-3"}
     * @param triggerTime 定时时间
     * @param maxRetryTimes 最大重试次数
     * @return
     */
    public Result submitTriggerTimeJob(String className, String method, Map<String, String> params, Date triggerTime,int maxRetryTimes) {
        Job job = new Job();
        Date date = new Date();
        int index = className.lastIndexOf(".");
        String jobId = className.substring(index + 1) + "_" + method + "_" + date.getTime();
        job.setTaskId(jobId);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("interface", className);
        paramMap.put("method", method);
        if (params != null && params.size() > 0) {
            paramMap.putAll(params);
        }
        job.setExtParams(paramMap);
        job.setTaskTrackerNodeGroup(nodeGroup);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);
        job.setTriggerTime(triggerTime.getTime());
        job.setMaxRetryTimes(maxRetryTimes);
        Response response = jobClient.submitJob(job);
        Result result = new Result();
        result.setJobId(jobId);
        result.setCode(response.getCode());
        result.setMsg(response.getMsg());
        result.setSuccess(response.isSuccess());
        return result;
    }

    /**
     * 取消定时器
     * 
     * @param jobId
     *            　　　增加时返回的jobId
     * @return
     */
    public Result cancelJob(String jobId) {
        Result result = new Result();
        Response response = jobClient.cancelJob(jobId, nodeGroup);
        result.setJobId(jobId);
        result.setCode(response.getCode());
        result.setMsg(response.getMsg());
        result.setSuccess(response.isSuccess());
        return result;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public JobCompletedHandler getJobCompletedHandler() {
        return jobCompletedHandler;
    }

    public void setJobCompletedHandler(JobCompletedHandler jobCompletedHandler) {
        this.jobCompletedHandler = jobCompletedHandler;
    }

    public JobClient<?, ?> getJobClient() {
        return jobClient;
    }
}
