// Custom scripts
$(document).ready(function () {

    // MetsiMenu
    $('#side-menu').metisMenu();

    // Collapse ibox function
    $('.collapse-link').click( function() {
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
    });

    // Close ibox function
    $('.close-link').click( function() {
        var content = $(this).closest('div.ibox');
        content.remove();
    });

    // Small todo handler
    $('.check-link').click( function(){
        var button = $(this).find('i');
        var label = $(this).next('span');
        button.toggleClass('fa-check-square').toggleClass('fa-square-o');
        label.toggleClass('todo-completed');
        return false;
    });

    // Append config box / Only for demo purpose
    $.get("skin-config.html", function (data) {
        $('body').append(data);
    });

    // minimalize menu
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    })

    // tooltips
    $('.tooltip-demo').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    })

    // Move modal to body
    // Fix Bootstrap backdrop issu with animation.css
    $('.modal').appendTo("body")

    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");
    }
    fix_height();

    // Fixed Sidebar
    // unComment this only whe you have a fixed-sidebar
            //    $(window).bind("load", function() {
            //        if($("body").hasClass('fixed-sidebar')) {
            //            $('.sidebar-collapse').slimScroll({
            //                height: '100%',
            //                railOpacity: 0.9,
            //            });
            //        }
            //    })

    $(window).bind("load resize click scroll", function() {
        if(!$("body").hasClass('body-small')) {
            fix_height();
        }
    })

    $("[data-toggle=popover]")
        .popover();
});


// For demo purpose - animation css script
function animationHover(element, animation){
    element = $(element);
    element.hover(
        function() {
            element.addClass('animated ' + animation);
        },
        function(){
            //wait for animation to finish before removing classes
            window.setTimeout( function(){
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

// Minimalize menu when screen is less than 768px
$(function() {
    $(window).bind("load resize", function() {
        if ($(this).width() < 769) {
            $('body').addClass('body-small')
        } else {
            $('body').removeClass('body-small')
        }
    })
})

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
        // Hide menu in order to smoothly turn on when maximize menu
        $('#side-menu').hide();
        // For smoothly turn on menu
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 100);
    } else if ($('body').hasClass('fixed-sidebar')){
        $('#side-menu').hide();
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 300);
    } else {
        // Remove all inline style from jquery fadeIn function to reset menu state
        $('#side-menu').removeAttr('style');
    }
}

// Dragable panels
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable(
        {
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8,
        })
        .disableSelection();
};


$(function($){
	/*jQuery插件$(select).method(params1, params2, ...)*/
	$.fn.extend({
		_jsonToForm:function(json){
			var form = $(this);
			if($.isPlainObject(json)){
				$.each(json,function(i,n){
					var field = form.find('input[name='+i+']');
					switch (field.attr('type')) {
					case 'hidden':
						field.val(n);
						break;
					case 'text':
						field.val(n);
						break;
					case 'email':
						field.val(n);
						break;
					case 'url':
						field.val(n);
						break;
					case 'radio':
						field.each(function(){
							var value = $(this).val();
							if(value == 'true'){value = true;}else if(value == 'false'){value = false;}
							if(value == n){$(this).get(0).checked = true;}
							});
						break;
					case 'checkbox':
						field.each(function(){
							var value = $(this).val();
							if(value == 'true'){value = true;}else if(value == 'false'){value = false;}
							if(value == n){$(this).get(0).checked = true;}
						});
						break;
					default:
						field = form.find('select[name='+i+']');
						if(!$.isEmptyObject(field.get(0))){
							field.children('option').each(function(){
								if($(this).val() == n){this.selected = true;}
							});
						}
						field = form.find('textarea[name='+i+']');
						if(!$.isEmptyObject(field.get(0))){
							field.val(n);
						}
						break;
					}
				});
			}
		},_formToJson:function(){
			var str = $(this).serialize();
				str = str.replace(/\+/g,"%20");
				str = str.replace(/&/g,"\",\"");
				str = str.replace(/=/g,"\":\"");
			var data = eval("({\""+str +"\"})");
			$.each(data,function(i,n){data[i] = $.trim(decodeURIComponent(n));});
			return data; 
		},_ajaxForm:function(callback,data){
			var target = $(this);
			var options = {url:$(this).attr('action'),type:'POST',beforeSubmit:function(){
				var submit = target.data('submit') ? false : true;
				target.data('submit', true);
				if(!submit) {
					asyncbox.tips('不可重复提交表单！', 'error');
				}
				return submit;
			},success: function(msg){
				if(typeof callback === 'function'){
					try {
						callback.call(target,msg);
					} catch (e) {
						$.messager.alert('操作提示','AJAX请求返回数据解析错误~','error');
						console.error('AJAX请求返回数据解析错误~['+e.message+']');
					}
				}
			}, complete : function(){
				target.data('submit', false);
			},data:data};
			$(this).ajaxSubmit(options);
		},_datagrid:function(params){
			var defaults = {
				pageSize : 30,
				nowrap : true,
				pagination : true,
				rownumbers : true,
				width : 'auto',
				height : 'auto',
				fitColumns : true,
				singleSelect : true,
				pageList:[10,20,30,50,100],
				striped : true,
				queryParams : {},
				onLoadError:function(e){
					$.messager.alert('操作提示','加载远程数据发生错误~','error');
				}
			};
			$.extend(defaults, params);
			$(this).datagrid(defaults);
			return this;
		}
	});
})
