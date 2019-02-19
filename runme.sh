#!/bin/bash

# If an error occurs, quit immediately
set -e

# Enable traps in all functions
set -E

# exit if a variable is unset
set -u

export PS4='+ \t \w '

declare -r txtblk='\e[0;30m' # Black - Regular
declare -r txtred='\e[0;31m' # Red
declare -r txtgrn='\e[0;32m' # Green
declare -r txtylw='\e[0;33m' # Yellow
declare -r txtblu='\e[0;34m' # Blue
declare -r txtpur='\e[0;35m' # Purple
declare -r txtcyn='\e[0;36m' # Cyan
declare -r txtwht='\e[0;37m' # White
declare -r txtrst='\e[0m'    # Text Reset
declare -r COLS=80

# https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

x=<<PROPERTIES
# GSON (GsonProperties)
spring.gson.date-format= # Format to use when serializing Date objects.
spring.gson.disable-html-escaping= # Whether to disable the escaping of HTML characters such as '<', '>', etc.
spring.gson.disable-inner-class-serialization= # Whether to exclude inner classes during serialization.
spring.gson.enable-complex-map-key-serialization= # Whether to enable serialization of complex map keys (i.e. non-primitives).
spring.gson.exclude-fields-without-expose-annotation= # Whether to exclude all fields from consideration for serialization or deserialization that do not have the "Expose" annotation.
spring.gson.field-naming-policy= # Naming policy that should be applied to an object's field during serialization and deserialization.
spring.gson.generate-non-executable-json= # Whether to generate non executable JSON by prefixing the output with some special text.
spring.gson.lenient= # Whether to be lenient about parsing JSON that doesn't conform to RFC 4627.
spring.gson.long-serialization-policy= # Serialization policy for Long and long types.
spring.gson.pretty-printing= # Whether to output serialized JSON that fits in a page for pretty printing.
spring.gson.serialize-nulls= # Whether to serialize null fields.

# Jackson JSON Parser
spring.jackson.serialization.indent_output=true
PROPERTIES

function usage() {
  echo -e "Usage: $0 COMMAND" >&2;
  echo -e "
  COMMAND - Spring-boot command to run. It can be one of the following:
    build-info
      Generate a build-info.properties file based the content of the current
      MavenProject.

    help
      Display help information on spring-boot-maven-plugin.
      Call mvn spring-boot:help -Ddetail=true -Dgoal=<goal-name> to display
      parameter details.

    repackage
      Repackages existing JAR and WAR archives so that they can be executed from the
      command line using java -jar. With layout=NONE can also be used simply to
      package a JAR with nested dependencies (and no main class, so not executable).

    run
      Run an executable archive application.

    start
      Start a spring application. Contrary to the run goal, this does not block and
      allows other goal to operate on the application. This goal is typically used
      in integration test scenario where the application is started before a test
      suite and stopped after.

    stop
      Stop a spring application that has been started by the 'start' goal. Typically
      invoked once a test suite has completed.
" >&2;
}

tomcatVersion=

# process the options
while getopts ":ht:" arg; do
    case $arg in
        h)
            usage;
            exit 0
            ;;
        t)
	    tomcatVersion=$OPTARG
            tomcat="-Dproject.properties.tomcat.version=${tomcatVersion}s" 
            ;;
        :)
            echo -e "${txtred}Option -$OPTARG requires an argument.${txtrst}" >&2
            usage;
            exit 2
            ;;
        \?)
            echo -e "${txtred}Invalid option: -$OPTARG${txtrst}" >&2
            usage;
            exit 1
            ;;
      esac
done

# remove parsed options from arguments
shift $((OPTIND-1))

command="run";

if [[ $# -eq 1 ]]; then
  if [[ $1 =~ build-info|help|repackage|run|start|stop ]]; then 
    command=$1 
  else 
    echo -e "${txtred}Error: COMMAND not recognized${txtrst}" 
    usage 
    exit 1
  fi
fi


# --spring.config.location

# Determine the location of this script. It should always be in the directory where application is built.
# -f canonicalize  by following every symlink in every component of the given 
# name recursively; all but the last component must exist
src=$(readlink -f "${BASH_SOURCE[0]}")

# Get the directory
dir=$(dirname "$src")


echo -e "${txtgrn}Invoking $src ...${txtrst}"
propertiesFile="${dir}/dev.properties" 
if [[ ! -e "$propertiesFile" ]]; then
  echo -e "${txtylw}No dev.properties found, using default...${txtrst}"
#else
#  echo -e "${txtylw}Using ${propertiesFile}...${txtrst}";
#  propertyOpt="--spring.config.location=file:${propertiesFile}"
  
fi
# serverPort="--server.port=9700"
mvn spring-boot:${command} -Dspring-boot.run.arguments="${propertyOpt:+${propertyOpt},}${serverPort:+${serverPort},}--spring.jackson.serialization.indent_output=true${propertyOpt:+,${propertyOpt}}"
