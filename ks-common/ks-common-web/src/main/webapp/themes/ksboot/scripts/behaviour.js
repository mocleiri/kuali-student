function initAMQListener() {
    var kualiSessionId = getKualiSessionId();
    var amq = org.activemq.Amq;
    amq.init({
        uri: 'amq',
        logging: true,
        timeout: 20,
        clientId: kualiSessionId
    });

    amq.addListener('theBrowser', 'org.kuali.student.user.message', function(msg) {
        var res = msg.textContent.split(":");
        showGrowl(res[1], '', res[0]);
    }, { selector: "JMSCorrelationID='" + kualiSessionId + "'" });

}

function getKualiSessionId() {
    var kualiSessionId = document.cookie.match(/kualiSessionId=[^;]+/);

    if(kualiSessionId == null)
        return '';

    if(typeof(kualiSessionId) == 'undefined')
        return '';

    if(kualiSessionId.length <= 0)
        return '';

    kualiSessionId = kualiSessionId[0];

    var end = kualiSessionId.lastIndexOf(';');
    if(end == -1) end = kualiSessionId.length;

    return kualiSessionId.substring(15, end);
}