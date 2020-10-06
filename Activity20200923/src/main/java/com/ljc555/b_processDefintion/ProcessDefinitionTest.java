package com.ljc555.b_processDefintion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**�������̶��壨��classpath��*/
	@Test
	public void deploymentProcessDefinition_classpath(){
		Deployment deployment = processEngine.getRepositoryService()//�����̶���Ͳ��������ص�Service
						.createDeployment()//����һ���������
						.name("���̶���")//��Ӳ��������
						.addClasspathResource("diagrams/helloworld.bpmn")//��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
						.addClasspathResource("diagrams/helloworld.png")//��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
						.deploy();//��ɲ���
		System.out.println("����ID��"+deployment.getId());//
		System.out.println("�������ƣ�"+deployment.getName());//
	}
	
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
	
	/**��ѯ���̶���*/
	@Test
	public void findProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//�����̶���Ͳ��������ص�Service
						.createProcessDefinitionQuery()//����һ�����̶���Ĳ�ѯ
						/**ָ����ѯ����,where����*/
//						.deploymentId(deploymentId)//ʹ�ò������ID��ѯ
//						.processDefinitionId(processDefinitionId)//ʹ�����̶���ID��ѯ
//						.processDefinitionKey(processDefinitionKey)//ʹ�����̶����key��ѯ
//						.processDefinitionNameLike(processDefinitionNameLike)//ʹ�����̶��������ģ����ѯ
						
						/**����*/
						.orderByProcessDefinitionVersion().asc()//���հ汾����������
//						.orderByProcessDefinitionName().desc()//�������̶�������ƽ�������
						
						/**���صĽ����*/
						.list();//����һ�������б���װ���̶���
//						.singleResult();//����Ωһ�����
//						.count();//���ؽ��������
//						.listPage(firstResult, maxResults);//��ҳ��ѯ
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				System.out.println("���̶���ID:"+pd.getId());//���̶����key+�汾+���������
				System.out.println("���̶��������:"+pd.getName());//��Ӧhelloworld.bpmn�ļ��е�name����ֵ
				System.out.println("���̶����key:"+pd.getKey());//��Ӧhelloworld.bpmn�ļ��е�id����ֵ
				System.out.println("���̶���İ汾:"+pd.getVersion());//�����̶����keyֵ��ͬ����ͬ�£��汾������Ĭ��1
				System.out.println("��Դ����bpmn�ļ�:"+pd.getResourceName());
				System.out.println("��Դ����png�ļ�:"+pd.getDiagramResourceName());
				System.out.println("�������ID��"+pd.getDeploymentId());
				System.out.println("#########################################################");
			}
		}			
	}
	
	/**ɾ�����̶���*/
	@Test
	public void deleteProcessDefinition(){
		//ʹ�ò���ID�����ɾ��
		String deploymentId = "601";
		/**
		 * ����������ɾ��
		 *    ֻ��ɾ��û�����������̣���������������ͻ��׳��쳣
		 */
//		processEngine.getRepositoryService()//
//						.deleteDeployment(deploymentId);
		
		/**
		 * ����ɾ��
		 * 	  ���������Ƿ����������ܿ���ɾ��
		 */
		processEngine.getRepositoryService()//
						.deleteDeployment(deploymentId, true);
		System.out.println("ɾ���ɹ���");
	}
	
	/**�鿴����ͼ
	 * @throws IOException */
	@Test
	public void viewPic() throws IOException{
		/**������ͼƬ�ŵ��ļ�����*/
		String deploymentId = "801";
		//��ȡͼƬ��Դ����
		List<String> list = processEngine.getRepositoryService()//
						.getDeploymentResourceNames(deploymentId);
		//����ͼƬ��Դ������
		String resourceName = "";
		if(list!=null && list.size()>0){
			for(String name:list){
				if(name.indexOf(".png")>=0){
					resourceName = name;
				}
			}
		}
		
		
		//��ȡͼƬ��������
		InputStream in = processEngine.getRepositoryService()//
						.getResourceAsStream(deploymentId, resourceName);
		
		//��ͼƬ���ɵ�D�̵�Ŀ¼��
		File file = new File("D:/"+resourceName);
		//����������ͼƬд��D����
		FileUtils.copyInputStreamToFile(in, file);
	}
	
	/***���ӹ��ܣ���ѯ���°汾�����̶���*/
	@Test
	public void findLastVersionProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
						.createProcessDefinitionQuery()//
						.orderByProcessDefinitionVersion().asc()//ʹ�����̶���İ汾��������
						.list();
		/**
		 * Map<String,ProcessDefinition>
  map���ϵ�key�����̶����key
  map���ϵ�value�����̶���Ķ���
  map���ϵ��ص㣺��map����keyֵ��ͬ������£���һ�ε�ֵ���滻ǰһ�ε�ֵ
		 */
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		if(pdList!=null && pdList.size()>0){
			for(ProcessDefinition pd:pdList){
				System.out.println("���̶���ID:"+pd.getId());//���̶����key+�汾+���������
				System.out.println("���̶��������:"+pd.getName());//��Ӧhelloworld.bpmn�ļ��е�name����ֵ
				System.out.println("���̶����key:"+pd.getKey());//��Ӧhelloworld.bpmn�ļ��е�id����ֵ
				System.out.println("���̶���İ汾:"+pd.getVersion());//�����̶����keyֵ��ͬ����ͬ�£��汾������Ĭ��1
				System.out.println("��Դ����bpmn�ļ�:"+pd.getResourceName());
				System.out.println("��Դ����png�ļ�:"+pd.getDiagramResourceName());
				System.out.println("�������ID��"+pd.getDeploymentId());
				System.out.println("#########################################################");
			}
		}	
	}
	
	/**���ӹ��ܣ�ɾ�����̶��壨ɾ��key��ͬ�����в�ͬ�汾�����̶��壩*/
	@Test
	public void deleteProcessDefinitionByKey(){
		//���̶����key
		String processDefinitionKey = "helloworld";
		//��ʹ�����̶����key��ѯ���̶��壬��ѯ�����еİ汾
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
						.createProcessDefinitionQuery()//
						.processDefinitionKey(processDefinitionKey)//ʹ�����̶����key��ѯ
						.list();
		//��������ȡÿ�����̶���Ĳ���ID
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				//��ȡ����ID
				String deploymentId = pd.getDeploymentId();
				processEngine.getRepositoryService()//
							.deleteDeployment(deploymentId, true);
			}
		}
	}
}
