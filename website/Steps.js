/*
 * model to hold steps data
 * - every geo-location data point
 */ 
class Steps {

    constructor(demo)
    {
        this.demo = demo
    }

    isEmpty() {
        if (this.listStep == null)
            return true;

        return false;
    }

    deserialize(stepsJsonString)
    {
        this.listStep = JSON.parse(stepsJsonString)
    }

    getStepAt(index)
    {
        if(this.listStep != null && 
            this.listStep.length >= index) {
            var step = this.listStep[index]
            return {lat: step.latitude, lng: step.longitude}
        }
        return null
    }

    getAll() {
        if(this.listStep != null && 
            this.listStep.length > 0) {
        
            var list = [];
            for (var i=0; i<this.listStep.length; i++) {
                list.push(this.getStepAt(i))
            }
            return list;
        }
        return null;
    }

    getDemo()
    {
        this.listStep = this.demo.getAll()
        return this.getAll();
    }
}