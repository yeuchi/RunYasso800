var goal = 240;
var demo = new Demo();
var splits = new Splits(demo);
var steps = new Steps(demo);

function filterSteps(str) {
    if(str!=null) {
        for(var i=0; i<str.length; i++) {
            if(str.charAt(i) == '[') {
                return str.substring(i, str.length) 
            }
        }
    }
    return str;
}

function persistGoal() {
    localStorage.setItem('goal', goal);
}

$(document).ready(function() {

    // init Steps textarea
    var stepsString = JSON.stringify(demo.getAll())
    localStorage.setItem('steps', stepsString);
    persistGoal()

    $("#txtSteps").val(stepsString)

    // click button Steps listener
    $("#btnSteps").on('click', function() {
      var str = $("#txtSteps").val();
      str = filterSteps(str)
      
      var goodSplits = splits.deserialize(str);
      var goodSteps = steps.deserialize(str);
      if(goodSplits && goodSteps)
        initMap();
      else 
        alert("Steps load error\n Your Steps JSON Array please.")
    });

    // click button Goal listener
    $("#btnGoal").on('click', function() {
      var time = $('#timeGoal').val().split(':')
      goal = parseInt(time[0]) * 60 + parseInt(time[1]);
      persistGoal();
      initMap();
    });

    // start map
    initMap()
  });