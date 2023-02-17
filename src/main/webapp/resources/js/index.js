const storedTheme = localStorage.getItem('theme');
	
function getPreferredTheme() {
	if(storedTheme) return storedTheme;
	return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
}
  
function setTheme(theme) {	// html 컬러변경
    if (theme === 'auto' && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    	$("html").attr("data-bs-theme", "dark");
    } else {
    	$("html").attr("data-bs-theme", theme);
    }
	$(".change-theme").removeClass("active");
    $("[data-bs-theme-value='" + theme + "']").addClass("active");
    $("#theme-icon").removeClass().addClass("fa-solid");
    
    if(theme === "light") { $("#theme-icon").addClass(["fa-sun", "text-warning"]);
    } else if(theme === "dark") { $("#theme-icon").addClass("fa-moon");
    } else if(theme === "auto") { $("#theme-icon").addClass(["fa-wand-magic-sparkles", "text-success"]); }
}
	
$(document).ready(function(){
	setTheme(getPreferredTheme());
	
	$(".change-theme").click(function(){
		let theme = $(this).attr("data-bs-theme-value");
		setTheme(theme);
		localStorage.setItem('theme', theme);
	});
	
	$("#theme-select").click(function(){
		$("#theme-list").addClass("show");
		$(this).attr("aria-expanded", "true");
	});

	
	// wing button
	$("#wingButton").click(function() {
		$("#wing").addClass("open");
		$("#wingMask").fadeIn();
	});

	// wing button - close
	$("#wingMask").click(function() {
		$("#wing").removeClass("open");
		$(this).css("display", "none");
	});
});