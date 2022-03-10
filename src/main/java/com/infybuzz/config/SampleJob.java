package com.infybuzz.config;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.infybuzz.model.StudentCsv;
import com.infybuzz.processor.FirstItemProcessor;
import com.infybuzz.reader.FirstItemReader;
import com.infybuzz.writer.FirstItemWriter;

@Configuration
public class SampleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private FirstItemReader firstItemReader;
	
	@Autowired
	private FirstItemProcessor firstItemProcessor;
	
	@Autowired
	private FirstItemWriter firstItemWriter;

	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())
				.build();
	}
	
	private Step firstChunkStep() {
		return stepBuilderFactory.get("First Chunk Step")
				.<StudentCsv, StudentCsv>chunk(3)
				.reader(flatFileItemReader())
				//.processor(firstItemProcessor)
				.writer(firstItemWriter)
				.build();
	}

	public FlatFileItemReader<StudentCsv> flatFileItemReader()
	{
		FlatFileItemReader<StudentCsv> flatFileItemReader = 
				new FlatFileItemReader<StudentCsv>();
		
		flatFileItemReader.setResource(new FileSystemResource(
				new File("C:\\Users\\devbh\\Downloads\\New folder (12)\\all-about-readers\\inputfiles\\students.csv")));
		
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID","First Name","Last Name","Email");
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});
			}
		});
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
}
