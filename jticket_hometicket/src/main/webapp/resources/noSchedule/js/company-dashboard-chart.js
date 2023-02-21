//$(function() {
//	// Set new default font family and font color to mimic Bootstrap's default styling
//	Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
//	Chart.defaults.global.defaultFontColor = '#292b2c';
//
//	var bigSize = 300;
//	var smallSize = 180;
//	
//	// Doughnut chart
//	var doughnutCtx = document.getElementById("salesPerCompanyChart");
//	var doughnutOptions = {
//	  cutoutPercentage: 60, // 도넛 두께: 값이 클수록 얇아짐
//	  maintainAspectRatio: false,
//	  legend: {
//	    position: 'right', 
//	    align: "center",
//	    fullWidth: true,
//	    padding: 10,
//	    labels: {
//	      fontSize: 14
//	    }
//	  },
//	  layout: {
//	    padding: {
//	      left: 0,
//	      right: 0,
//	      top: 10,
//	      bottom: 10
//	    }
//	  }
//	}
//
//	var salesPerCompanyChart = new Chart(doughnutCtx, {
//	  type: 'doughnut',
//	  data: {
//	    labels: ["성류굴", "아쿠아리움", "안전체험관", "곤충여행관", "과학체험관"],
//	    datasets: [{
//	      data: [25, 25, 25, 21, 4],
//	      backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745', '#e8eb34'],
//	    }],
//	  },  
//	  options: doughnutOptions
//	});
//
//
//
//	// 판매수량 / 판매금액 차트
//	var barCtx = document.getElementById("salesBarChart");
//	drawChart(barCtx);
//	var myLineChart = new Chart(barCtx, {
//	  type: 'bar',
//	  data: {
//	    labels: ["성류굴", "아쿠아리움", "안전체험관", "곤충여행관", "과학체험관"],
//	    datasets: [
//	      {
//	        barThickness: 6,
//	        label: "판매수량",
//	        backgroundColor: "rgba(236,128,141,1)",
//	        borderColor: "rgba(2,117,216,1)",
//	        data: [4000, 5000, 6000, 7000, 8000]
//	      },
//	      {
//	        barThickness: 6,
//	        label: "판매금액",
//	        backgroundColor: "rgba(245,153,53,1)",
//	        borderColor: "rgba(2,117,216,1)",
//	        data: [3000, 7000, 6000, 2000, 9000]
//	      },
//	    ],
//	  },
//	  options: {
//	    scales: {
//	      xAxes: [{
//	        time: {
//	          unit: 'month'
//	        },
//	        gridLines: {
//	          display: false
//	        },
//	        ticks: {
//	          maxTicksLimit: 6
//	        }
//	      }],
//	      yAxes: [{
//	        ticks: {
//	          min: 0,
//	          max: 15000,
//	          maxTicksLimit: 5
//	        },
//	        gridLines: {
//	          display: true
//	        }
//	      }],
//	    },
//	    legend: {
//	      display: true,
//	    }
//	  }
//	});
//
//
//
//
//	// 시간별 방문자 수
//
//	var randomScalingFactor = function() {
//	  return Math.round(Math.random() * 100);
//	};
//
//
//	//backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745', '#e8eb34'],
//	var hourConfig = {
//	  type: 'line',
//	  data: {
//	    labels: ["0",	"1",	"2",	"3",	"4",	"5",	"6",	"7",	"8",	"9",	"10",	"11",	"12",	"13",	"14",	"15",	"16",	"17",	"18",	"19",	"20",	"21",	"22",	"23"],
//	    datasets: [{
//	      label: '성류굴',
//	      backgroundColor: "rgba(163,0,20,1)",
//	      borderColor: "rgba(163,0,20,1)",
//	      data: [
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor()
//	      ],
//	      fill: false,
//	    },
//	    {
//	      label: '아쿠아리움',
//	      fill: false,
//	      backgroundColor: "rgba(128,128,255,1)",
//	      borderColor: "rgba(128,128,255,1)",
//	      data: [
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor()
//	      ],
//	    },
//	    {
//	      label: '안전체험관',
//	      fill: false,
//	      backgroundColor: "rgba(22,155,213,1)",
//	      borderColor: "rgba(22,155,213,1)",
//	      data: [
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor()
//	      ],
//	    },
//	    {
//	      label: '곤충여행관',
//	      fill: false,
//	      backgroundColor: "rgba(245,154,35,1)",
//	      borderColor: "rgba(245,154,35,1)",
//	      data: [
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor()
//	      ],
//	    },
//	    {
//	      label: '과학체험관',
//	      fill: false,
//	      backgroundColor: "rgba(255,255,0,1)",
//	      borderColor: "rgba(255,255,0,1)",
//	      data: [
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor(),
//	        randomScalingFactor()
//	      ],
//	    }]
//	  },
//	  
//	  options: {
//	    responsive: true,
//	    // title: {
//	    //   display: true,
//	    //   text: 'Chart.js Line Chart'
//	    // },
//	    tooltips: {
//	      mode: 'index',
//	      intersect: false,
//	    },
//	    hover: {
//	      mode: 'nearest',
//	      intersect: true
//	    },
//	    scales: {
//	      xAxes: [{
//	        display: true,
//	        scaleLabel: {
//	          display: true,
//	          labelString: '시간'
//	        }
//	      }],
//	      yAxes: [{
//	        display: true,
//	        scaleLabel: {
//	          display: true,
//	          labelString: '방문자'
//	        },
//	        ticks: {
//	          min: 0,
//	          max: 100,
//	          stepSize: 100
//
//	          // forces step size to be 5 units
//	        }
//	      }]
//	    },
//	    elements: {
//	      line: {
//	        tension: 0
//	      }
//	    }
//	  }
//	};
//
//	var hourLineCtx = document.getElementById('visitorPerHourLineChart');
//	drawChart(hourLineCtx);
//	var visitorPerHourLineChart = new Chart(hourLineCtx, hourConfig);
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//	// 월별 방문자 수
//
//	var randomScalingFactor = function() {
//	  return Math.round(Math.random() * 100);
//	};
//
//
//	var monthConfig = {
//	  type: 'line',
//	  data: 
//	  {
//	    labels: ["1월",	"2월",	"3월",	"4월",	"5월",	"6월",	"7월",	"8월",	"9월",	"10월",	"11월",	"12월"],
//	    datasets: [
////	    	{
////	      label: '성류굴',
////	      backgroundColor: "rgba(163,0,20,1)",
////	      borderColor: "rgba(163,0,20,1)",
////	      data: [
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor()
////	      ],
////	      fill: false,
////	    },
////	    {
////	      label: '아쿠아리움',
////	      fill: false,
////	      backgroundColor: "rgba(128,128,255,1)",
////	      borderColor: "rgba(128,128,255,1)",
////	      data: [
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor()
////	      ],
////	    },
////	    {
////	      label: '안전체험관',
////	      fill: false,
////	      backgroundColor: "rgba(22,155,213,1)",
////	      borderColor: "rgba(22,155,213,1)",
////	      data: [
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor()
////	      ],
////	    },
////	    {
////	      label: '곤충여행관',
////	      fill: false,
////	      backgroundColor: "rgba(245,154,35,1)",
////	      borderColor: "rgba(245,154,35,1)",
////	      data: [
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor()
////	      ],
////	    },
////	    {
////	      label: '과학체험관',
////	      fill: false,
////	      backgroundColor: "rgba(255,255,0,1)",
////	      borderColor: "rgba(255,255,0,1)",
////	      data: [
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor(),
////	        randomScalingFactor()
////	      ],
////	    }
//			]
//	  },
//	  
//	  options: {
//	    responsive: true,
//	    // title: {
//	    //   display: true,
//	    //   text: 'Chart.js Line Chart'
//	    // },
//	    tooltips: {
//	      mode: 'index',
//	      intersect: false,
//	    },
//	    animation: {
//	    	duration: 0
//	    },
//	    hover: {
//	    	animationDuration: 0
////	      mode: 'nearest',
////	      intersect: true
//	    },
//	    responsiveAnimationDuration: 0,
//	    scales: {
//	      xAxes: [{
//	        display: true,
//	        scaleLabel: {
//	          display: true,
//	          labelString: '시간'
//	        }
//	      }],
//	      yAxes: [{
//	        display: true,
//	        scaleLabel: {
//	          display: true,
//	          labelString: '방문자'
//	        },
//	        ticks: {
//	          min: 0,
//	          max: 100,
//
//	          // forces step size to be 5 units
//	        }
//	      }]
//	    },
//	    elements: {
//	      line: {
//	        tension: 0
//	      }
//	    }
//	  }
//	};
//
//	var monthLineCtx = document.getElementById('visitorPerMonthLineChart');
//	drawChart(monthLineCtx);
//	var visitorPerHourLineChart = new Chart(monthLineCtx, monthConfig);
//	
//	// 그래프 사이즈 조절
//	function drawChart(ctx){
//		if($(window).width() < 576){
//			ctx.height = bigSize;
//		} else{
//			ctx.height = smallSize;
//		}
//	}
//	
//	function getWidth() {
//		return Math.max(document.body.scrollWidth,
//				document.documentElement.scrollWidth,
//				document.body.offsetWidth,
//				document.documentElement.offsetWidth,
//				document.documentElement.clientWidth);
//	}
//
//	function getHeight() {
//		return Math.max(document.body.scrollHeight,
//				document.documentElement.scrollHeight,
//				document.body.offsetHeight,
//				document.documentElement.offsetHeight,
//				document.documentElement.clientHeight);
//	}
//	
////	label: '과학체험관',
////    fill: false,
////    backgroundColor: "rgba(255,255,0,1)",
////    borderColor: "rgba(255,255,0,1)",
////    data: [
//	function drData(){
//		const dataset = visitorPerHourLineChart.data.datasets;
////		for(var i=0; i < 5; i++){
//		<c:out value="">
//		'<c:forEach var="item" begin="0" end="5">'
////			dataset[i].push("label: data-" + i);
////			dataset[i].push("fill: " + false);
////			dataset[i].push("backgroundColor: " + "rgba(255,255," + (20 * i) + ",1)");
////			dataset[i].push("borderColor: " + "rgba(255,255," + (20 * i) + ",1)");
////			dataset[i].push("data: " + randomData());
////			var str = randomData();
//			const newDatasets = {
//					label: "data-" + '${item}',
//					fill: false,
//					backgroundColor: "rgba(255,255," + '${item * 20}' + ",1)",
//					borderColor: "rgba(255,255," + '${item * 20}' + ",1)",
//					data: [randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ,randomScalingFactor()
//						  ]
//			};
//			dataset.push(newDatasets);
//		'</c:forEach>'
////		}
//		visitorPerHourLineChart.update();
//	};
//	
//	function randomData(){
//		var str = {};
//		for(var i=0; i<12; i++){
//			var str2 = "\"" + randomScalingFactor() + "\"";
//			if(i > 0){
//				str[i] = "\," + str2;
//			} else {
//				str[i] = str2;
//			}
//		}
//		return str;
//	}
//
//	
//	drData();
//});
//
