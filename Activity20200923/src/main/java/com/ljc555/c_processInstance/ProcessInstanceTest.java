package com.ljc555.c_processInstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**�������̶��壨��zip��*/
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ��������ص�Service
						.createDeployment()//����һ���������
						.name("���̶���")//��Ӳ��������
						.addZipInputStream(zipInputStream)//ָ��zip��ʽ���ļ���ɲ���
						.deploy();//��ɲ���
		System.out.println("����ID��"+deployment.getId());//
		System.out.println("�������ƣ�"+deployment.getName());//
	}
	
	/**��������ʵ��*/
	@Test
	public void startProcessInstance(){
		//���̶����key
		String processDefinitionKey = "hellowrold";
		ProcessInstance pi = processEngine.getRuntimeService()//������ִ�е�����ʵ����ִ�ж�����ص�Service
						.startProcessInstanceByKey(processDefinitionKey);//ʹ�����̶����key��������ʵ����key��Ӧhelloworld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID:"+pi.getId());//����ʵ��ID    101
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());//���̶���ID   helloworld:1:4
	}
	
	/**��ѯ��ǰ�˵ĸ�������*/
	@Test
	public void findMyPersonalTask(){
		String assignee = "����";
		List<Task> list = processEngine.getTaskService()//������ִ�е����������ص�Service
						.createTaskQuery()//���������ѯ����
						/**��ѯ������where���֣�*/
						.taskAssignee(assignee)//ָ�����������ѯ��ָ��������
//						.taskCandidateUser(candidateUser)//������İ����˲�ѯ
//						.processDefinitionId(processDefinitionId)//ʹ�����̶���ID��ѯ
//						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
//						.executionId(executionId)//ʹ��ִ�ж���ID��ѯ
						/**����*/
						.orderByTaskCreateTime().asc()//ʹ�ô���ʱ�����������
						/**���ؽ����*/
//						.singleResult()//����Ωһ�����
//						.count()//���ؽ����������
//						.listPage(firstResult, maxResults);//��ҳ��ѯ
						.list();//�����б�
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("����ID:"+task.getId());
				System.out.println("��������:"+task.getName());
				System.out.println("����Ĵ���ʱ��:"+task.getCreateTime());
				System.out.println("����İ�����:"+task.getAssignee());
				System.out.println("����ʵ��ID��"+task.getProcessInstanceId());
				System.out.println("ִ�ж���ID:"+task.getExecutionId());
				System.out.println("���̶���ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	/**����ҵ�����*/
	@Test
	public void completeMyPersonalTask(){
		//����ID
		String taskId = "15004";
		processEngine.getTaskService()//������ִ�е����������ص�Service
					.complete(taskId);
		System.out.println("�����������ID��"+taskId);
	}
	
	/**��ѯ����״̬���ж���������ִ�У����ǽ�����*/
	@Test
	public void isProcessEnd(){
		String processInstanceId = "1001";
		ProcessInstance pi = processEngine.getRuntimeService()//��ʾ����ִ�е�����ʵ����ִ�ж���
						.createProcessInstanceQuery()//��������ʵ����ѯ
						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
						.singleResult();
		if(pi==null){
			System.out.println("�����Ѿ�����");
		}
		else{
			System.out.println("����û�н���");
		}
	}
	
	/**��ѯ��ʷ���񣨺��潲��*/
	@Test
	public void findHistoryTask(){
		String taskAssignee = "����";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()//����ʷ���ݣ���ʷ����ص�Service
						.createHistoricTaskInstanceQuery()//������ʷ����ʵ����ѯ
						.taskAssignee(taskAssignee)//ָ����ʷ����İ�����
						.list();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				System.out.println(hti.getId()+"    "+hti.getName()+"    "+hti.getProcessInstanceId()+"   "+hti.getStartTime()+"   "+hti.getEndTime()+"   "+hti.getDurationInMillis());
				System.out.println("################################");
			}
		}
	}
	
	/**��ѯ��ʷ����ʵ�������潲��*/
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId = "1001";
		HistoricProcessInstance hpi = processEngine.getHistoryService()//����ʷ���ݣ���ʷ����ص�Service
						.createHistoricProcessInstanceQuery()//������ʷ����ʵ����ѯ
						.processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ
						.singleResult();
		System.out.println(hpi.getId()+"    "+hpi.getProcessDefinitionId()+"    "+hpi.getStartTime()+"    "+hpi.getEndTime()+"     "+hpi.getDurationInMillis());
	}
}
