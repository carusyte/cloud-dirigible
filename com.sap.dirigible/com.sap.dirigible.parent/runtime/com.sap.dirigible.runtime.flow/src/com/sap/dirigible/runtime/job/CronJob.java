package com.sap.dirigible.runtime.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.utils.EngineUtils;

public class CronJob implements Job {
	
	private static final Logger logger = Logger.getLogger(CronJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.debug("Starting Job...");
		String instName = context.getJobDetail().getName();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String jobType = dataMap.getString(JobParser.NODE_TYPE);
		String jobModule = dataMap.getString(JobParser.NODE_MODULE);
		logger.debug(String.format("Job processing name: %s, type: %s, module: %s ...", instName, jobType, jobModule));
		Map<Object, Object> executionContext = new HashMap<Object, Object>();
		Object inputOutput = null;
		try {
			inputOutput = executeByEngineType(null, null, jobModule, executionContext, instName, inputOutput, jobType);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug(String.format("Job name: %s, type: %s, module: %s finished.", instName, jobType, jobModule));
	}
	
	
	public static Object executeByEngineType(HttpServletRequest request,
			HttpServletResponse response, String module,
			Map<Object, Object> executionContext, String jobName,
			Object inputOutput, String type) throws IOException {
		if (ICommonConstants.ENGINE_TYPE.JAVASCRIPT.equalsIgnoreCase(type)) {
			inputOutput = EngineUtils.executeJavascript(request, response,
					executionContext, inputOutput, module);
//				} else if (ICommonConstants.ENGINE_TYPE.JAVA.equalsIgnoreCase(flowStep.getType())) {
//					inputOutput = EngineUtils.executeJava(request, response,
//						executionContext, inputOutput, flowStep);
		} else if (ICommonConstants.ENGINE_TYPE.COMMAND.equalsIgnoreCase(type)) {
			inputOutput = EngineUtils.executeCommand(request, response, executionContext,
					inputOutput, module);
		} else if (ICommonConstants.ENGINE_TYPE.FLOW.equalsIgnoreCase(type)) {
			inputOutput = EngineUtils.executeFlow(request, response, executionContext,
					inputOutput, module);
		} else { // groovy etc...
			throw new IllegalArgumentException(String.format("Unknown execution type [%s] of %s at %s", 
					type, jobName, module));
		}
		return inputOutput;
	}

}