package job.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import javax.batch.runtime.BatchStatus;

import job.util.AbstractJobTest;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SplitJobTest extends AbstractJobTest{
	
	@Test
	public void test() throws Exception {
		BatchStatus status = startJob("split", new Properties()).getBatchStatus();
		assertThat(status, is(BatchStatus.COMPLETED));
	}
}
