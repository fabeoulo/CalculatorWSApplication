var pXa = 0;
var pYa = 0;

var mapInfo = {
    titleName: "M棟3樓IDS",
    x: 680,
    y: 0
};

var titleGroup = [
    {lineName: "IDS-L1", lineNameHtml: "L1", x: 195, y: 380},
    {lineName: "IDS-L2", lineNameHtml: "L2", x: 345, y: 380},
    {lineName: "IDS-PKG_L1", lineNameHtml: "PKG_L1", x: 125, y: 85},
    {lineName: "IDS-PKG_L2", lineNameHtml: "PKG_L2", x: 335, y: 85}
];

//, straight: true, reverse: true
var testGroup = [
    {people: 1, x: 215, y: 285},
    {people: 1, x: 340, y: 285}
];

var babGroup = [
    {people: 2, x: 215, y: 315, lineName: "IDS-L1", straight: true},
    {people: 2, x: 340, y: 315, lineName: "IDS-L2", straight: true},
    {people: 3, x: 215, y: 150, lineName: "IDS-PKG_L1", straight: true},
    {people: 3, x: 330, y: 150, lineName: "IDS-PKG_L2", straight: true},
    {people: 1, x: 530, y: 375, lineName: "IDS_CR1"},
    {people: 1, x: 530, y: 375, lineName: "M3F_CR1"},
    {people: 1, x: 530, y: 345, lineName: "IDS_CR2"},
    {people: 1, x: 530, y: 345, lineName: "M3F_CR2"},
    {people: 1, x: 530, y: 300, lineName: "IDS_CR3"},
    {people: 1, x: 530, y: 300, lineName: "M3F_CR3"},
    {people: 1, x: 530, y: 270, lineName: "IDS_CR4"},
    {people: 1, x: 530, y: 270, lineName: "M3F_CR4"},
    {people: 1, x: 530, y: 240, lineName: "IDS_CR5"},
    {people: 1, x: 530, y: 240, lineName: "M3F_CR5"},
    {people: 1, x: 530, y: 160, lineName: "IDS_CR6"},
    {people: 1, x: 530, y: 160, lineName: "M3F_CR6"},
    {people: 1, x: 530, y: 110, lineName: "IDS_CR7"},
    {people: 1, x: 530, y: 110, lineName: "M3F_CR7"},
    {people: 1, x: 710, y: 230, lineName: "IDS_CR8"},
    {people: 1, x: 710, y: 230, lineName: "M3F_CR8"},
    {people: 1, x: 840, y: 230, lineName: "IDS_CR9"},
    {people: 1, x: 840, y: 230, lineName: "M3F_CR9"},
    {people: 1, x: 840, y: 200, lineName: "IDS_CR10"},
    {people: 1, x: 840, y: 200, lineName: "M3F_CR10"},
    {people: 1, x: 910, y: 400, lineName: "IDS_CR11"},
    {people: 1, x: 910, y: 400, lineName: "M3F_CR11"},
    {people: 1, x: 1000, y: 400, lineName: "IDS_CR12"},
    {people: 1, x: 1000, y: 400, lineName: "M3F_CR12"}
];

var fqcGroup = [
];

var minTestTableNo = 50;
var maxTestTableNo = 51; // no use now

var sitefloor = 3; //for test table

