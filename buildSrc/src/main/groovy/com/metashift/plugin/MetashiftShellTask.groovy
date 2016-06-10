package com.minds.metashift.plugin

import org.gradle.api.internal.file.collections.SimpleFileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.springframework.boot.loader.tools.FileUtils
import org.springframework.boot.gradle.run.SourceSets

class MetashiftShellTask extends JavaExec {

    @Override
    public void exec() {
        SourceSet mainSourceSet = SourceSets.findMainSourceSet(getProject());
        final File outputDir = (mainSourceSet == null ? null : mainSourceSet.getOutput().getResourcesDir());
        final Set<File> resources = new LinkedHashSet<File>();

        if (mainSourceSet != null) {
            resources.addAll(mainSourceSet.getResources().getSrcDirs());
        }

        List<File> classPath = new ArrayList<File>(getClasspath().getFiles());
        classPath.addAll(0, resources);
        getLogger().info("Adding classpath: " + resources);
        setClasspath(new SimpleFileCollection(classPath));
        if (outputDir != null) {
            for (File directory : resources) {
                FileUtils.removeDuplicatesFromOutputDirectory(outputDir, directory);
            }
        }

        this.setStandardInput(System.in)

        super.exec();
    }

}
