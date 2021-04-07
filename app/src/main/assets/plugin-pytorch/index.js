

module.exports = function(plugin){
    const runtime = plugin.runtime;
    const scope = plugin.topLevelScope;

    function pytorch(){
    }

    pytorch.debugOd = function(weightPath,classPath){
        weightPath = weightPath || "";
        classPath = classPath || "";
        plugin.debugOd(weightPath,classPath)
    }
    pytorch.liveOd = function(weightPath,classPath){
        weightPath = weightPath || "";
        classPath = classPath || "";
        plugin.liveOd(weightPath,classPath)
    }
    pytorch.debugTec = function(weightPath,vocabPath){
        weightPath = weightPath || ""
        vocabPath = vocabPath || ""
        plugin.debugTec(weightPath,vocabPath)
    }

    pytorch.getIOU = function(a,b){
        return plugin.getIOU(a,b)
    }
    pytorch.useNMS = function(boxes,limit,threshold){
        limit = limit || 20;
        threshold = threshold || 0.30;
        return plugin.useNMS(boxes,limit,threshold)
    }

    pytorch.load = function(path,device){
        device = device || 0;
        path = path || "";
        return plugin.load(path,device)
    }
    pytorch.forward = function(module,input){
        return plugin.forward(module,input)
    }
    pytorch.forwardTuple = function(module,input){
        return plugin.forwardTuple(module,input)
    }
    pytorch.destory = function(module){
        module.destory()
    }

    pytorch.fromBlob = function(arr,shape){
        return plugin.fromBlob(arr,shape)
    }

    pytorch.getCocoClasses = function(){
        return plugin.getCocoClasses()
    }
    pytorch.getTextcnnVocab = function(){
        return plugin.getTextcnnVocab()
    }

    pytorch.bitmapToTensor = function(b,mean,std){
        mean = mean || [0.485, 0.456, 0.406];
        std = std || [0.229, 0.224, 0.225];
        return plugin.bitmapToTensor(b,mean,std);
    }
    pytorch.floatsToResults = function(floats,row,column,imgScaleX,imgScaleY,threshold){
        threshold = threshold || 0.3
        return plugin.floatsToResults(floats,row,column,imgScaleX,imgScaleY,threshold)
    }

    pytorch.simplifySentence = function(str){
        return plugin.simplifySentence(str)
    }
    pytorch.vocab = function(words){
        return plugin.vocab(words)
    }
    pytorch.vocabPath = function(path){
        return plugin.vocabPath(path)
    }


    return pytorch;
}