/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2009-2010 Rostislav Hristov, Asual DZZD. All rights reserved.
 *
 * This product is owned by Asual DZZD. All rights in the product including copyrights, licensing rights, 
 * patents, trademarks, engineering rights, moral rights, and any other intellectual property rights 
 * belong to Asual DZZD. These rights are not transferred as part of this agreement. 
 *
 * No part of the product may be reproduced, published, transmitted electronically, mechanically or 
 * otherwise, transcribed, stored in a retrieval system or translated into any language in any form, by 
 * any means, for any purpose other than the purchaser's personal use, without the express written 
 * permission of Asual DZZD.
 */

(function($) {
	
    $(function() {
	    
        (function(scope) {
        	
        	var fn = arguments.callee;
            
        	$('*', scope).filter('[data-ajax]').each(function() {

                var url, data, event,
	                o = $(this), 
	                ids = o.attr('data-ajax'),
	                method = o.attr('data-ajax-method'),
	                regions = $('#' + ids.replace(/:/g, '\\:').split(' ').join(', #'));
            
	            if (o.is('a')) {
	                url = o.attr('href');
	                event = 'click';
	            } else if (o.is('button')) {
	                var form = o.parents('form');
	                url = form.attr('action');
	                data = form.serialize();
	                event = 'click';
	            }
            
            	o.bind(event, function() {
            		
                    $.ajax({
                        url: url,
                        type: method ? method : 'post',
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
                        	regions.each(function(i) {
                        		fn($(this).html($(data.getElementsByTagName('update')[i].firstChild.nodeValue).html()));
                            });
                        	regions.trigger('success', [data, status, xhr]);
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