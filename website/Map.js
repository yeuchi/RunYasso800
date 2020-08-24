    let map;
    var goal = 240;
    var demo = new Demo();
    var splits = new Splits(demo);
    var steps = new Steps(demo);

    function setColor(index, time) {
      if(index%2==0) {
        return "blue";
      }
      
      return time < goal ? "green" : "red";
    }

    function setYassoMarkers(map, list, goal) {
      for(var i=0; i<list.length; i++) {
        // set color
        var color = setColor(i, list[i].time)
        var j = i+1;
        setMarker(color, j, list[i])
      }
    }

    function setMarker(color, label, latlng) {
      var image = {url: "https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_"+color+label+".png"};
      var marker = new google.maps.Marker({position: latlng,icon: image});
      marker.setMap(map);
    }

    function setPath(map, list) {
      var runPath = new google.maps.Polyline({
        path: list,
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2
      });

      runPath.setMap(map);
    }

    function initMap() {
      var runPath = steps.isEmpty() ? steps.getDemo() : steps.getAll();
      var markers = splits.isEmpty() ? splits.getDemo() : splits.getAll();

      if(typeof google != 'undefined') {

        // midpoint might be better
        var meanPoint = splits.findCenter()
        if(meanPoint != null) {
          map = new google.maps.Map(document.getElementById('map'), {
            zoom: 14,
            center: meanPoint,
            mapTypeId: 'terrain'
          });
        }

        setPath(map, runPath)                               // the running path
        setYassoMarkers(map, markers, goal)                 // 1 - N jog/sprint markers
        setMarker("white", 'F', runPath[runPath.length-1])  // Finish marker
      }
    }

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

    