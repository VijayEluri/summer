/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function($) {
	
    $(function() {
	    
        (function(scope) {
        	
        	var fn = arguments.callee;
            
        	$('*', scope).filter('[data-ajax]').each(function() {

                var url, 
                	event = 'click',
	                o = $(this), 
	                re = /^(get|post)$/i,
	                ids = o.attr('data-ajax'),
	                data = o.attr('data-ajax-params'),
	                method = o.attr('data-ajax-method'),
	                regions = $('#' + ids.replace(/:/g, '\\:').split(' ').join(', #'));
                
	            if (o.is('a')) {
	                url = o.attr('href');
	                method = method ? method : 'get';
	                if (!re.test(method)) {
	                	data += (data ? '&' : '') + '_method=' + method;
	                }
                } else if (o.is('button, input, select, textarea')) {
	                var form = o.parents('form');
	                url = form.attr('action');
	                data = (data ? data + '&' : '') + form.serialize();	            	
	                method = method ? method : (form.attr('method') ? form.attr('method') : 'post');
	                if (!re.test(method)) {
	                	data = data.replace(/(^|&)_method=[^&]*(&|$)/, '$1_method=' + method + '$2')
	                }
	                if (!o.is('button')) {
		                event = 'blur';
	                }
	            }

	            if (o.attr('data-ajax-url')) {
	            	url = o.attr('data-ajax-url');
	            }
	            
	            if (o.attr('data-ajax-event')) {
	            	event = o.attr('data-ajax-event');
	            }
	            
            	o.bind(event, function() {
            		
                    $.ajax({
                        url: url,
                        type: re.test(method) ? method : 'post',
                        data: 'javax.faces.partial.ajax=true&javax.faces.partial.render=' + ids + (data ? '&' + data : ''),
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader('Faces-Request', 'partial/ajax');
                            regions.trigger('beforeSend', [xhr]);
                        },
                        complete: function(xhr, status) {
                        	regions.trigger('complete', [xhr, status]);
                        },
                        error: function(xhr, status, error) {
                            if (xhr.status) {
                                this.success(xhr.responseXML, [xhr, status, error]);
                            }
                            regions.trigger('error');
                        },
                        success: function(data, status, xhr) {
                        	if (data && data.getElementsByTagName('update').length > 0) {
	                        	regions.each(function(i) {
	                        		fn($(this).html($(data.getElementsByTagName('update')[i].firstChild.nodeValue).html()));
	                            });
	                        	regions.trigger('success', [data, status, xhr]);
                        	} else {
	                        	regions.trigger('error', [data, status, xhr]);                        		
                        	}
                        }
                    });
                    
                    return false;
                    
                });            	
            	
            });
        	
        })(document);
        
        //$('.region').bind('beforeSend', function() {
        //}).bind('success', function() {
        //});	    
		
    });
    
})(jQuery);