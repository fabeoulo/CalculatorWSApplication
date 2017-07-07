//Js檔用來給予下拉式表單使用(unit可編輯欄位需要區隔，記錄在db之中)
var worktimeCol = [
    {name: "id", editable: false},
    {name: "modelName"},
    {name: "type.id"},
    {name: "productionWt"},
    {name: "totalModule"},
    {name: "setupTime"},
    {name: "cleanPanel"},
    {name: "assy"},
    {name: "t1"},
    {name: "t2"},
    {name: "t3"},
    {name: "t4"},
    {name: "packing"},
    {name: "upBiRi"},
    {name: "downBiRi"},
    {name: "biCost"},
    {name: "vibration"},
    {name: "hiPotLeakage"},
    {name: "coldBoot"},
    {name: "warmBoot"},
    {name: "assyToT1"},
    {name: "t2ToPacking"},
    {name: "floor.id"},
    {name: "pending.id"},
    {name: "pendingTime"},
    {name: "burnIn"},
    {name: "biTime"},
    {name: "biTemperature"},
    {name: "userBySpeOwnerId.id"},
    {name: "userByEeOwnerId.id"},
    {name: "userByQcOwnerId.id"},
    {name: "assyPackingSop"},
    {name: "testSop"},
    {name: "keypartA"},
    {name: "keypartB"},
    {name: "preAssy.id"},
    {name: "flowByBabFlowId.id"},
    {name: "flowByTestFlowId.id"},
    {name: "flowByPackingFlowId.id"},
    {name: "partLink"},
    {name: "ce"},
    {name: "ul"},
    {name: "rohs"},
    {name: "weee"},
    {name: "madeInTaiwan"},
    {name: "fcc"},
    {name: "eac"},
    {name: "nsInOneCollectionBox"},
    {name: "partNoAttributeMaintain"},
    {name: "assyStation"},
    {name: "packingStation"},
    {name: "assyLeadTime"},
    {name: "assyKanbanTime"},
    {name: "packingLeadTime"},
    {name: "packingKanbanTime"},
    {name: "cleanPanelAndAssembly"},
    {name: "modifiedDate", editable: false},
    {name: "bwAvgViews.0.assyAvg", editable: false},
    {name: "bwAvgViews.0.packingAvg", editable: false}
];