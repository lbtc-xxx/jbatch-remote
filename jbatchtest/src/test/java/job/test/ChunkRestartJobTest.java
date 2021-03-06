package job.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import job.util.AbstractJobTest;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ChunkRestartJobTest extends AbstractJobTest{
	
	@Test
	@UsingDataSet({"job/chunk/ChunkInputItem.yml", "job/chunk/ChunkOutputItem.yml"})
	@ShouldMatchDataSet(value = "job/chunkrestart/expected0-2.yml", orderBy = "id")
	public void itemReaderFailAt5() throws Exception {
		Properties props = new Properties();
		props.setProperty("divide", "2");
		props.setProperty("itemReaderFailAt", "5");
		
		BatchStatus status = startJob("chunkrestart", props).getBatchStatus();
		assertThat(status, is(BatchStatus.FAILED));
	}
	
	@Test
	@UsingDataSet({"job/chunk/ChunkInputItem.yml", "job/chunk/ChunkOutputItem.yml"})
	@ShouldMatchDataSet(value = "job/chunk/expected.yml", orderBy = "id")
	public void itemReaderRestart() throws Exception {
		Properties props = new Properties();
		props.setProperty("divide", "2");
		props.setProperty("itemReaderFailAt", "5");
		
		JobExecution jobExecution = startJob("chunkrestart", props);
		assertThat(jobExecution.getBatchStatus(), is(BatchStatus.FAILED));
		
		props.remove("itemReaderFailAt");
		JobExecution jobExecutionAtRestart = restartJob(jobExecution.getExecutionId(), props);
		assertThat(jobExecutionAtRestart.getBatchStatus(), is(BatchStatus.COMPLETED));
	}

}
