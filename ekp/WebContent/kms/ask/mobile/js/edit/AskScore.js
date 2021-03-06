define(
		[ "dojo/_base/declare", "dojo/dom-construct", "dojo/dom-class",
				"dijit/_WidgetBase", "dojo/_base/array", "dojo/topic",
				"dojo/dom-attr" ], function(declare, domConstruct, domClass,
				widgetBase, array, topic, domAttr) {

			return declare("kms.ask.AskScore", widgetBase, {

				baseClass : 'muiAskScore',

				scores : [ 0, 5, 10, 20, 30, 50, 70, 100 ],

				score : 100,

				selectedClass : 'muiAskScoreItemSelected',

				normalClass : 'muiAskScoreItem',

				disableClass : 'muiAskScoreItemDisable',

				br : 4,

				scoreChange : '/kms/ask/scoreChange',

				buildRendering : function() {
					this.inherited(arguments);

					// 隐藏域
					this.inputNode = domConstruct.create('input', {
						type : 'hidden',
						name : 'fdScore',
						value : 0
					}, this.domNode);

					// 头部节点
					this.titleNode = domConstruct.create('div', {
						className : 'muiAskScoreTitle',
						innerHTML : '选择悬赏值'
					}, this.domNode);

					// 分数选择模板节点
					this.scoreNode = domConstruct.create('div', {
						className : 'muiAskScoreContent'
					}, this.domNode);

					// 底部节点
					this.footNode = domConstruct.create('div', {
						className : 'muiAskScoreFoot',
						innerHTML : '您剩余可用的财富值：'
					}, this.domNode);

					// 剩余货币节点
					this.countNode = domConstruct.create('span', {
						className : 'muiAskScoreCount',
						innerHTML : this.score
					}, this.footNode);

					array.forEach(this.scores, function(item, index) {
						if (index % this.br == 0) {
							this.currentParent = domConstruct.create('div', {
								className : 'muiAskScoreRow'
							}, this.scoreNode);
						}
						this.buildItem(item, this.currentParent);
					}, this);

					this.connect(this.scoreNode, 'click', 'onSelect');
//					this.subscribe(this.scoreChange, '_scoreChange');
				},

				_scoreChange : function(obj, evt) {
					if (!evt)
						return;
					var score = evt.score;
					this.countNode.innerHTML = this.score - score;
					this.inputNode.value = score;
				},

				onSelect : function(evt) {
					var target = evt.target;
					while (target) {
						if (domClass.contains(target, this.normalClass)) {
							var score = parseInt(domAttr.get(target,
									'data-score'));
							if (domClass.contains(target, this.disableClass)) {
								break;
							}
							array.forEach(this.itemArray, function(item) {
								domClass.remove(item, this.selectedClass);
							}, this);
							domClass.add(target, this.selectedClass);
							topic.publish(this.scoreChange, this, {
								score : score
							});
							break;
						}
						target = target.parentNode;
					}
				},

				itemArray : [],

				buildItem : function(item, parent) {
					var className = this.normalClass;
					if (this.score < item)
						className += " " + this.disableClass;
					if (item == 0)
						className += " " + this.selectedClass;
					this.itemArray.push(domConstruct.create('div', {
						'data-score' : item,
						className : className,
						innerHTML : '<span>' + item + '</span>'
					}, parent));

				}
			});
		});