package com.techustle.billablehour.v1.backend.service

import com.techustle.billablehour.v1.backend.model.Project
import com.techustle.billablehour.v1.backend.repository.ProjectRepository
import com.techustle.billablehour.v1.backend.service.TimesheetService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration

@Configuration
class TimesheetServiceTests {
	@InjectMocks
	lateinit var timesheetService : TimesheetService
	@Mock
	lateinit var projectRepository: ProjectRepository

	@BeforeEach
	fun setup() {
		MockitoAnnotations.initMocks(this)
	}

	@Test
	fun saveProjectTest() {
		var project: Project = Project()
		project.Id = 1
		project.name = "MTN"
		project.remarks = "Project created for MTN"
		Mockito.`when`(projectRepository.findProjectByName(anyString())).thenReturn(project)
		var proj: Project = timesheetService.saveProject("MTN")
		//check if the return project is not null
		Assertions.assertNotNull(proj)
		Assertions.assertEquals("MTN", proj.name)
	}
	@Test
	fun saveJob() {

	}


}
