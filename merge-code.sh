#!/bin/bash 

# merge-code.sh
# Written by Orlando Ramirez (orlando.ramirezmartinez@utoronto.ca)
# Tries to merge code 
#

#Define variables
debug=0
version="1.0.0"

#Define configuration variables

function version(){
    echo "version ${version}"
    exit 0
}


function dbgprint(){
    if [ $debug -ne 0 ]; then
	msg="`date +%Y-%m-%d-%H:%M`:"
	for arg in $*
	do
	    msg="$msg $arg"
	done
	echo -e "$msg"
    fi
    return 0
}


function usage(){
    echo "${script_name} version ${version}"
    echo ""
    echo " --debug"
    echo "   Enable debug messages during execution"
    echo " --version"
    echo "   Print version and exit"
    echo " --help"
    echo "   Print this message and exit"
    echo " --current_feature | -c"
    echo "   Path were is the feature being develop"
    echo " --merge_path | -m"
    echo "   Temp repository where the merge will be made"
    echo " --trunk | -t"
    echo "   Display logs after a each action gets executed"
    echo ""
    echo " Quickstart:"
    echo "" 
    echo " To run the script, do the following"
    echo " merge-code.sh -c <Current_Feature_Path> -m <Merge_Path> -t <Trunk_Path>"
    echo ""
    exit 0
}

function exit_error(){
    echo "$1":$2:"$3"
    exit $2
}


function check_variables(){
dbgprint ${FUNCNAME[0]}

echo 1 cf ${CURRENT_FEATURE}
echo 2 mp ${MERGE_PATH}
echo 3 t ${TRUNK_PATH}

if  `svn ls ${CURRENT_FEATURE} > /dev/null 2>&1` ; then
    dbgprint "Variable CURRENT_FEATURE is verified"
    if `svn ls ${TRUNK_PATH} > /dev/null 2>&1` ; then
	dbgprint "Variable TRUNK_PATH is verified"
    else
	exit_error "${FUNCNAME[0]}" 2 "Svn Trunk (-t) not defined correctly"
    fi
else
    exit_error "${FUNCNAME[0]}" 1 "Svn Current_Feature (-c) not defined correctly"
fi

}

function clean_repository(){
dbgprint ${FUNCNAME[0]}

if `svn ls ${MERGE_PATH} > /dev/null 2>&1` ; then
    svn delete ${MERGE_PATH} -m "Deleted merge-test repository"
    if [ $? -ne 0 ]; then
	exit_error "${FUNCNAME[0]}" 3 "Script can't delete repository. Please check."
    fi
else
    dbgprint "MERGE_PATH repo doesn't exist"
fi
 
}

function create_merge_repository(){
dbgprint ${FUNCNAME[0]}

svn cp ${CURRENT_FEATURE} ${MERGE_PATH} -m "Creating Merge Repo"
if [ $? -ne 0 ]; then
    exit_error "${FUNCNAME[0]}" 4 "Script can't create temp_repository. Please check."
fi

}


function checkout_merge_repository(){
dbgprint ${FUNCNAME[0]}

svn co ${MERGE_PATH}
if [ $? -ne 0 ]; then
    exit_error "${FUNCNAME[0]}" 5 "Script can't checkout temp_repository. Please check."
fi

}

function merge_code(){
dbgprint ${FUNCNAME[0]}

merge_dir=`echo ${MERGE_PATH##*/}`

cd ${merge_dir}

svn merge -q --non-interactive ${TRUNK_PATH}
if [ $? -ne 0 ]; then 
    svn status | grep ^C
    exit_error "${FUNCNAME[0]}" 6 "Script detected an error while doing the merge. Please check."
else
    echo "Code Merged"
fi

}


shortopts="c:m:t:"
longopts="current_feature: merge_path: version help debug trunk:"
args=`getopt -n$0 -u -a -q --options="$shortopts"  --longoptions="$longopts" "h" "$@"`
set -- $args || usage

while [ $# -gt 0 ]
do 
    case "$1" in 
	-c|--current_feature)
	    CURRENT_FEATURE=`echo ${2}`;  
	    shift;;
	-m|--merge_path)
	    MERGE_PATH=`echo ${2}`;  
	    shift;;
	-t|--trunk)  
	    TRUNK_PATH=`echo ${2}`;  
	    shift;;
	--help)        
	    usage;;
	--version)     
	    version;; 
	--debug)       
	    let "debug+=1";; 
    esac
    shift
done

######################################################################################
#
# merge-code.sh -c <CURRENT_FEATURE_REPO> -m <MERGED_REPO> -t <MASTER_REPO>--debug
#
######################################################################################

check_variables
clean_repository
create_merge_repository 
checkout_merge_repository
merge_code
