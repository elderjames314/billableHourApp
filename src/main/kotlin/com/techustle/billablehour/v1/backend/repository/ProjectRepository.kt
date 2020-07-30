package com.techustle.billablehour.v1.backend.repository

import com.techustle.billablehour.v1.backend.model.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Int> {

    //The  function below will fetch project by project name.
    //please note that the project name has to be specific eg MTN will be different MTNs
    fun findProjectByName(name:String): Project?
}