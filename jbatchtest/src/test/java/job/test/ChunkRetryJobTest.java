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
public class ChunkRetryJobTest extends AbstractJobTest{
	
	@Test
	@UsingDataSet({"job/chunk/ChunkInputItem.yml", "job/chunk/ChunkOutputItem.yml"})
	@ShouldMatchDataSet(value = "job/chunk/expected.yml", orderBy = "id")
	public void itemReaderRestart() throws Exception {
		Properties props = new Properties();
		props.setProperty("divide", "2");
		props.setProperty("itemReaderFailAt", "5");
		props.setProperty("itemReaderFails", "3");
		
		JobExecution jobExecution = startJob("chunkretry", props);
		assertThat(jobExecution.getBatchStatus(), is(BatchStatus.COMPLETED));
	}

}
