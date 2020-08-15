/*
 * model to hold splits data
 * - geo-location data points that mark the start / stop of a split.
 */ 
class Splits {

    constructor(demo)
    {
       this.demo = demo
    }

    isEmpty() {
        if (this.listSplit == null)
            return true;

        return false;
    }

    deserialize(stepsJsonString)
    {
        var json = JSON.parse(stepsJsonString)
        parse(json)
    }

    parse(json) {
        this.listSplit = [];
        if(json != null && json.length>0) {
            var splitIndex = -1
            for(var i=0; i<json.length; i++){
                var step = json[i]
                if(step.splitIndex > splitIndex) {
                    splitIndex = step.splitIndex
                    var colorStr = (splitIndex % 2==0)?"blue":"green"
                    this.listSplit.push({lat:step.latitude, lng:step.longitude, color:colorStr})
                }
            }
        }
    }

    getStartAt(index) {
        if(this.listSplit != null && 
            this.listSplit.length >= index) {
            return this.listSplit[index]
        }
        return null;
    }

    getEndAt(index)
    {
        if(this.listSplit != null && 
            this.listSplit.length >= index) {
            var split = this.listSplit[index]
            return {lat: split.endLat, lng: split.endLong}
        }
        return null;
    }

    getAll() {
        if(this.listSplit != null && 
            this.listSplit.length > 0) {
        
            var list = [];
            for (var i=0; i<this.listSplit.length; i++) {
                list.push(this.getStartAt(i))
            }
            return list;
        }
        return null;
    }

    getDemo()
    {/*
        this.listSplit = [
            {"dis":814.8068237304688,"endLat":44.9174924,"endLong":-93.3160292,"endTime":1597402189062,"meetGoal":true,"run_type":"jog","splitIndex":0,"startLat":44.9112725,"startLong":-93.3175595,"startTime":1597401897269},
            {"dis":811.99951171875,"endLat":44.915937,"endLong":-93.3076769,"endTime":1597402443542,"meetGoal":false,"run_type":"sprint","splitIndex":1,"startLat":44.9175766,"startLong":-93.3158391,"startTime":1597402196740},
            {"dis":853.5977172851562,"endLat":44.9187389,"endLong":-93.2991897,"endTime":1597402715000,"meetGoal":true,"run_type":"jog","splitIndex":2,"startLat":44.9159522,"startLong":-93.3075117,"startTime":1597402450964},
            {"dis":804.4898071289062,"endLat":44.9248595,"endLong":-93.2964289,"endTime":1597402960645,"meetGoal":true,"run_type":"sprint","splitIndex":3,"startLat":44.918731,"startLong":-93.2991916,"startTime":1597402723108},
            {"dis":814.95947265625,"endLat":44.9286216,"endLong":-93.3043098,"endTime":1597403211229,"meetGoal":true,"run_type":"jog","splitIndex":4,"startLat":44.9250243,"startLong":-93.2964729,"startTime":1597402968656},
            {"dis":812.8139038085938,"endLat":44.9275788,"endLong":-93.3097468,"endTime":1597403481877,"meetGoal":false,"run_type":"sprint","splitIndex":5,"startLat":44.9285957,"startLong":-93.304322,"startTime":1597403218895},
            {"dis":809.59716796875,"endLat":44.92144,"endLong":-93.3132496,"endTime":1597403730289,"meetGoal":true,"run_type":"jog","splitIndex":6,"startLat":44.9273706,"startLong":-93.3096776,"startTime":1597403489497},
            {"dis":808.24853515625,"endLat":44.915371,"endLong":-93.3163511,"endTime":1597403995953,"meetGoal":false,"run_type":"sprint","splitIndex":7,"startLat":44.9208664,"startLong":-93.3133643,"startTime":1597403739036},
            {"dis":579.4636840820312,"endLat":44.9112146,"endLong":-93.3175156,"endTime":1597404252853,"meetGoal":true,"run_type":"jog","splitIndex":8,"startLat":44.9150937,"startLong":-93.316286,"startTime":1597404003552}];
            */
        var demo = new Demo()
        var json = demo.getAll()
        this.parse(json)
        return this.getAll();
    }
}